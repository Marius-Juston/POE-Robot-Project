import glob
import os
from datetime import datetime

import cv2
import numpy as np


class Camera:
    def __init__(self, cameraPort) -> None:
        self.camera_port = cameraPort
        self.camera = cv2.VideoCapture(cameraPort)
        self.photo_folder = "./Camera{0:d} Photos/".format(self.camera_port)
        self.matrix_file = './Camera{0:d}_matrix.npz'.format(self.camera_port)
        self.matrix = None

        if os.path.exists(self.matrix_file):
            self.matrix = np.load(self.matrix_file)

            self.objpoints, self.imgpoints, self.ret, self.mtx, self.dist, self.rvecs, self.tvecs, self.newcameramtx, self.roi = [
                self.matrix[i] for i in
                ("objpoints", "imgpoints", "ret", "mtx", 'dist', 'rvecs', 'tvecs', 'newcameramtx', 'roi')]

        if not os.path.exists(self.photo_folder):
            os.mkdir(self.photo_folder)

    def get_frame(self):
        ret, frame = self.camera.read()
        return frame

    def get_undistorted_frame(self, cropped=True):
        img = self.get_frame()

        if self.matrix is not None:
            img = cv2.undistort(img, self.mtx, self.dist, None, self.newcameramtx)

            if cropped:
                x, y, w, h = self.roi
                img = img[y:y + h, x:x + w]

        return img

    def close(self):
        self.camera.release()


def take_calibration_photos_together(leftCamera, rightCamera, rows=7, cols=7, release=True):
    while True:
        imgL = leftCamera.get_frame()
        imgR = rightCamera.get_frame()

        grayL = cv2.cvtColor(imgL, cv2.COLOR_BGR2GRAY)
        grayR = cv2.cvtColor(imgR, cv2.COLOR_BGR2GRAY)

        # Find the chess board corners
        retL, cornersL = cv2.findChessboardCorners(grayL, (rows, cols), None)
        retR, cornersR = cv2.findChessboardCorners(grayR, (rows, cols), None)

        if retL:
            cv2.drawChessboardCorners(imgL, (rows, cols), cornersL, retL)
        if retR:
            cv2.drawChessboardCorners(imgR, (rows, cols), cornersR, retR)

        cv2.imshow("Camera {0:d}".format(leftCamera.camera_port), imgL)
        cv2.imshow("Camera {0:d}".format(rightCamera.camera_port), imgR)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

        if cv2.waitKey(1) & 0xFF == ord(' '):
            now = datetime.now().strftime("Photo_%Y_%m_%d_%H_%M_%S.png")
            cv2.imwrite(leftCamera.photo_folder + now, imgL)
            cv2.imwrite(rightCamera.photo_folder + now, imgR)

    if release:
        leftCamera.close()
        rightCamera.close()

    cv2.destroyAllWindows()


def save_undistort_matrix(*cameras, rows=7, cols=7, show=False):
    for camera in cameras:
        # termination criteria
        criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 30, 0.001)

        # prepare object points, like (0,0,0), (1,0,0), (2,0,0) ....,(6,5,0)
        objp = np.zeros((cols * rows, 3), np.float32)
        objp[:, :2] = np.mgrid[0:rows, 0:cols].T.reshape(-1, 2)

        # Arrays to store object points and image points from all the images.
        objpoints = []  # 3d point in real world space
        imgpoints = []  # 2d points in image plane.

        images = glob.glob(camera.photo_folder + '*.png')
        gray = None

        for fname in images:
            img = cv2.imread(fname)
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

            # Find the chess board corners
            ret, corners = cv2.findChessboardCorners(gray, (rows, cols), None)

            # If found, add object points, image points (after refining them)
            if ret:
                objpoints.append(objp)

                cv2.cornerSubPix(gray, corners, (11, 11), (-1, -1), criteria)
                imgpoints.append(corners)

                if show:
                    # Draw and display the corners
                    cv2.drawChessboardCorners(img, (rows, cols), corners, ret)
                    cv2.imshow('img', img)
                    cv2.waitKey(500)

        if show:
            cv2.destroyWindow("img")

        if gray is not None:
            w, h = int(camera.camera.get(4)), int(camera.camera.get(3))

            ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, (w, h), None, None)
            newcameramtx, roi = cv2.getOptimalNewCameraMatrix(mtx, dist, (w, h), 1, (w, h))

            if show:
                for fname in images:
                    img = cv2.imread(fname)
                    imgH, imgW = img.shape[:2]

                    assert imgH == h or imgW == w, "Image width({0:d}) and height({1:d}) must be the same as camera " \
                                                   "resolution({2:d}x{3:d}).".format(imgW, imgH, w, h)

                    # undistort
                    dst = cv2.undistort(img, mtx, dist, None, newcameramtx)

                    cv2.imshow("img undistorted", dst)
                    cv2.waitKey(1000)

                cv2.destroyWindow("img undistorted")

            mean_error = 0
            for i in range(len(objpoints)):
                imgpoints2, _ = cv2.projectPoints(objpoints[i], rvecs[i], tvecs[i], mtx, dist)
                error = cv2.norm(imgpoints[i], imgpoints2, cv2.NORM_L2) / len(imgpoints2)
                mean_error += error

            print("total error: ", mean_error / len(objpoints))

            np.savez(camera.matrix_file, objpoints=objpoints, imgpoints=imgpoints, ret=ret, mtx=mtx, dist=dist,
                     rvecs=rvecs, tvecs=tvecs, newcameramtx=newcameramtx, roi=roi)

    cv2.destroyAllWindows()


def extrinsic_calibration(leftCamera, rightCamera):
    termination_criteria_extrinsics = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 100, 0.001)
    w, h = int(leftCamera.camera.get(4)), int(rightCamera.camera.get(3))
    assert w == int(rightCamera.camera.get(4)) and h == int(
        rightCamera.camera.get(3)), "Cameras must have the same width and height settings"

    (rms_stereo, camera_matrix_l, dist_coeffs_l, camera_matrix_r, dist_coeffs_r, R, T, E, F) = cv2.stereoCalibrate(
        leftCamera.objpoints, leftCamera.imgpoints, rightCamera.imgpoints, leftCamera.mtx, leftCamera.dist,
        rightCamera.mtx, rightCamera.dist, (w, h), criteria=termination_criteria_extrinsics, flags=0)

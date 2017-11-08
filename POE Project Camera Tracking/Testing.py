from FaceTracking import Camera, extrinsic_calibration

leftCamera = Camera(0)
rightCamera = Camera(1)

# take_calibration_photos_together(leftCamera, rightCamera,release=False)

# save_undistort_matrix(leftCamera, rightCamera)

extrinsic_calibration(leftCamera, rightCamera)

#
# leftCamera.calibrate_camera()
# rightCamera.calibrate_camera()
#
# i = 0
# while True:
#     imgL = leftCamera.get_frame()
#     imgR = rightCamera.get_frame()
#
#     break

leftCamera.close()
rightCamera.close()

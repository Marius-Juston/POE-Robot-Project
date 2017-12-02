package org.waltonrobotics.curvedrawer.util;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Dashboard {

  private static NetworkTable table;
  private static String dashboard = "SmartDashboard";
  private static int teamNumber = 2974;
  private static String[] ipAddresses;

  static {
    initSmartDashboard();
  }

  private static String[] setIpAddresses(String... extraIpAddresses) {
    String[] ipAddresses = new String[5 + extraIpAddresses.length];
    ipAddresses[0] = "10." + teamNumber / 100 + "." + teamNumber % 100 + ".2";
    ipAddresses[1] = "172.22.11.2";
    ipAddresses[2] = "roboRIO-" + teamNumber + "-FRC.local";
    ipAddresses[3] = "roboRIO-" + teamNumber + "-FRC.lan";
    ipAddresses[4] = "roboRIO-" + teamNumber + "-FRC.frc-field.local";

    System.arraycopy(extraIpAddresses, 0, ipAddresses,
        ipAddresses.length - extraIpAddresses.length, extraIpAddresses.length);

    NetworkTable.setIPAddress(ipAddresses);
    return ipAddresses;
  }

  private static void initSmartDashboard() {
    setTeamNumber(teamNumber);
    NetworkTable.setClientMode();
    ipAddresses = setIpAddresses();

    table = getNetworkTable(dashboard);
  }

  public static void putString(String key, double string) {
    table.putNumber(key, string);
  }

  public static String getString(String key, String defaultValue) {
    return table.getString(key, defaultValue);
  }

  public static NetworkTable getTable() {
    return table;
  }

  public static String getDashboard() {
    return dashboard;
  }

  public static void setDashboard(String dashboard) {
    Dashboard.dashboard = dashboard;
    table = getNetworkTable(dashboard);
  }

  public static NetworkTable getNetworkTable(String dashboard) {
    return NetworkTable.getTable(dashboard);
  }

  public static int getTeamNumber() {
    return teamNumber;
  }

  public static void setTeamNumber(int teamNumber) {
    Dashboard.teamNumber = teamNumber;
    NetworkTable.setTeam(teamNumber);
  }

  public static String[] getIpAddresses() {
    return ipAddresses;
  }
}

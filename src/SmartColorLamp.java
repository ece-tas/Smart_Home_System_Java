import java.io.IOException;
import java.util.List;

public class SmartColorLamp extends SmartDevices {

    private String initialStatus;
    private int brightness;
    private int colorCode;
    private int kelvinValue;





    public SmartColorLamp(String devices, String names, String initialStat,
                          String color, String bright, String[] removedTab,
                          List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(devices, names, removedTab,addNames, allDevicesList);
        this.initialStatus = initialStat;

        if (!addNames.contains(names)) {
            if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                try {
                    int intColorCode = Integer.decode(color);
                    if (intColorCode <= 0xFFFFFF && intColorCode >= 0x000000) {
                        this.colorCode = intColorCode;
                        try {
                            int intBrightness = Integer.parseInt(bright);
                            if (intBrightness >= 0 && intBrightness <= 100) {
                                this.brightness = intBrightness;
                                addNames.add(names);
                                allDevicesList.add(new String[] {getDevice(), getNames(), getInitialStatus(),
                                        getColorCode(), getBrightness(), null});
                            } else {
                                FileOutput.writeToFile("ERROR: Brightness must be in range of 0%-100%!");
                            }
                        } catch (NumberFormatException ee) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                    }

                } catch (NumberFormatException | IOException e) {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }
            } else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with the same name!");
        }
    }


    public String getInitialStatus() {
        return initialStatus;
    }
    public String getColorCode() {
        return getRemovedTab()[4];
    }

    public String getBrightness() {
        return getRemovedTab()[5];
    }
}






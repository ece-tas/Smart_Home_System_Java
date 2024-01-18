import java.io.IOException;
import java.util.List;

public class SmartLamp extends SmartDevices {
    private String initialStatus;
    private int kelvinValue;
    private int brightness;


    public SmartLamp
            (String devices, String names, String initialStat,
             String kelvin, String bright, String[] removedTab, List<String> addNames,  List<String[]> allDevicesList) throws IOException {

        super(devices, names,  removedTab, addNames, allDevicesList);
        this.initialStatus = initialStat;


        if (!addNames.contains(getNames())) {
            if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                try{
                    int intKelvin = Integer.parseInt(kelvin);
                    if (intKelvin >= 2000 && intKelvin <= 6500) {
                        this.kelvinValue = intKelvin;
                        try{
                            int intBrightness = Integer.parseInt(bright);
                            if (intBrightness >= 0 && intBrightness <= 100) {
                                this.brightness = intBrightness;
                                addNames.add(getNames());
                                allDevicesList.add(new String[]{getDevice(), getNames(), getInitialStatus(),
                                        getKelvinValue(), getBrightness(), null});
                            } else {
                                FileOutput.writeToFile("ERROR: Brightness must be in range of 0%-100%!");
                            }
                        } catch (NumberFormatException ee) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Kelvin value must be in range of 2000K-6500K!");
                    }
                } catch (NumberFormatException | IOException e) {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }
            } else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");

        }
    }


    public SmartLamp(String devices, String names, String initialStat,
                     String[] removedTab, List<String> addNames,  List<String[]> allDevicesList) throws IOException {

        super(devices, names,  removedTab, addNames, allDevicesList);
        this.initialStatus = initialStat;
        this.kelvinValue = 4000;
        this.brightness = 100;

        if (!addNames.contains(getNames())) {
            if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                addNames.add(getNames());
                allDevicesList.add(new String[]{getDevice(), getNames(), getInitialStatus(),
                        Integer.toString(kelvinValue), Integer.toString(brightness), null});
            } else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    public SmartLamp(String devices, String names, String[] removedTab,
                     List<String> addNames, List<String[]> allDevicesList) throws IOException {
        super(devices, names,  removedTab, addNames, allDevicesList);
        this.initialStatus = "Off";
        this.kelvinValue = 4000;
        this.brightness = 100;
        if (!addNames.contains(getNames())) {
            addNames.add(getNames());
            allDevicesList.add(new String[]{getDevice(), getNames(), initialStatus,
                    Integer.toString(kelvinValue), Integer.toString(brightness), null});
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }




    public String getInitialStatus() {
        return getRemovedTab()[3];
    }

    public String getKelvinValue() {
        return getRemovedTab()[4];
    }
    public void setKelvinValue(int kelvinValue) {
        this.kelvinValue = kelvinValue;
    }


    public String getBrightness() {
        return getRemovedTab()[5];
    }

    public void setKelvin(int kelvin) throws IOException {
        if (kelvin >= 2000 && kelvin <= 6500) {
            this.kelvinValue = kelvin;
            updateDeviceList();
        } else {
            FileOutput.writeToFile("ERROR: Kelvin value must be in range of 2000K-6500K!");
        }
    }

    private void updateDeviceList() {
        for (String[] line : getAllDevicesList()) {
            if (line[1].equals(getNames())) {
                line[3] = String.valueOf(getKelvinValue());
            }
        }
    }

}

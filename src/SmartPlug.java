import java.io.IOException;
import java.util.List;

public class SmartPlug extends SmartDevices {

    private String initialStatus;
    private double ampere;


    public SmartPlug (String devices, String names, String initialStat,
                      String amp, String[] removedTab, List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(devices, names, removedTab, addNames, allDevicesList);
        this.initialStatus = initialStat;

        if (!addNames.contains(getNames())) {
            if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                try{
                    double doubleAmpere = Double.parseDouble(amp);
                    if (doubleAmpere > 0) {
                        this.ampere = doubleAmpere;
                        addNames.add(getNames());
                        allDevicesList.add(new String[]{getDevice(), getNames(),
                                getInitialStatus(), getStrAmpere(), null});


                    } else {
                        FileOutput.writeToFile("ERROR: Ampere value must be a positive number!");
                    }
                } catch (NumberFormatException e) {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }
            }
            else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    public SmartPlug (String devices, String names, String initialStat,
                      String[] removedTab, List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(devices, names, removedTab, addNames, allDevicesList);
        this.initialStatus = initialStat;

        if (!addNames.contains(getNames())) {
            if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                addNames.add(getNames());
                allDevicesList.add(new String[] {getDevice(), getNames(), getInitialStatus(), null, null});
            } else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    public SmartPlug (String devices, String names, String[] removedTab,
                      List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(devices, names, removedTab, addNames, allDevicesList);
        this.initialStatus = "Off";
        if (!addNames.contains(getNames())) {
            addNames.add(getNames());
            allDevicesList.add(new String[] {getDevice(), getNames(), initialStatus, null, null});

        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    public String getInitialStatus(){
        return initialStatus;
    }

    private String getStrAmpere(){
        return getRemovedTab()[4];
    }

    private double getAmpere(){
        return ampere;
    }
}

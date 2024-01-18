import java.io.IOException;
import java.util.List;

public class SmartCamera extends SmartDevices {

    private double megaByteConsumption;
    private String initialStatus;


    public SmartCamera(String device, String names, String megaByte, String initialStat,
                       String[] removedTab,List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(device, names, removedTab, addNames, allDevicesList);
        this.initialStatus = initialStat;

        if (!addNames.contains(getNames())) {
                try {
                    double doubleMegaByte = Double.parseDouble(megaByte);
                    if (doubleMegaByte > 0) {
                        this.megaByteConsumption = doubleMegaByte;
                        if (initialStatus.equals("On") || initialStatus.equals("Off")) {
                            addNames.add(getNames());
                            allDevicesList.add(new String[]{getDevice(), getNames(), Double.toString(getMegaByteConsumption()),
                                    getInitialStatus(), null});
                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");

                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Megabyte value must be a positive number!");
                    }

                } catch (NumberFormatException e) {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    public SmartCamera(String device, String names, String megaByte, String[] removedTab,
                       List<String> addNames,  List<String[]> allDevicesList) throws IOException {
        super(device, names, removedTab, addNames, allDevicesList);
        this.initialStatus = "Off";
        if (!addNames.contains(getNames())) {
            try {
                double doubleMegaByte = Double.parseDouble(megaByte);
                if (doubleMegaByte > 0) {
                    this.megaByteConsumption = doubleMegaByte;
                    addNames.add(getNames());
                    allDevicesList.add(new String[] {getDevice(), getNames(), Double.toString(getMegaByteConsumption()),
                    initialStatus, null});
                } else {
                    FileOutput.writeToFile("ERROR: Megabyte value must be a positive number!");
                }

            } catch (NumberFormatException | IOException e) {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
        } else {
            FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
        }
    }


    private String getStrMegaByteConsumption(){
        return  getRemovedTab()[3];
    }
    private double getMegaByteConsumption() {
        return megaByteConsumption;
    }


    public String getInitialStatus() {
        return initialStatus;
    }
}

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class SmartDevices {

    private String[] removedTab;
    private List<String> addNames;

    String device;
    String name;
    List<String[]> allDevicesList;



    public SmartDevices(String devices, String names, String[] removedTab,
                        List<String> addNames, List<String[]> allDevicesList) {
        this.device = devices;
        this.name = names;
        this.removedTab = removedTab;
        this.addNames = addNames;
        this.allDevicesList = allDevicesList;
    }


    public String getDevice(){
        return device;
    }

    public String getNames(){
        return name;
    }

    public String[] getRemovedTab(){
        return removedTab;
    }
    public List<String[]> getAllDevicesList() {
        return allDevicesList;
    }

}
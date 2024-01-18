
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;


public class Commands2 {

    private final FileInput file;
    private final FileOutput output;
    private TimeParser timeParser;
    private DateTimeParser dateTimeParser;
    private DateTimeFormatter formatter;

    private LocalDateTime currentTime = null;
    private LocalDateTime initialTime = null;
    private String smallestSwitchTime = null;
    private String eachSwitchTime = null;
    private String strNopSwitchedTime = null;

    private List<String[]> allDevicesList = new ArrayList<>();
    private List<String> addNames = new ArrayList<>();
    List<String> switchTimes = new ArrayList<>();


    public Commands2(String filePath, String fileOutput) throws IOException {

        dateTimeParser = new DateTimeParser();

        output = new FileOutput(fileOutput);
        file = new FileInput();
        String[] inputFile = file.readFile(filePath);

        int countInitialTime = 0;

        for (int i = 0; i < inputFile.length; i++) {

            String commandLine = inputFile[i];
            String[] removedTab = commandLine.split("\t");

            timeParser = new TimeParser(removedTab, smallestSwitchTime, eachSwitchTime);



            FileOutput.writeToFile("COMMAND: " + commandLine);
            

            if (!inputFile[0].contains("SetInitialTime")) {

                FileOutput.writeToFile("ERROR: First command must be set initial time! Program is going to terminate!");
                break;
            }


            else if (containsSetInitialTime(commandLine)) {

                if (boolLengthRemovedTab(removedTab, 2)) {
                    if (i == 0) {
                        String strTime = removedTab[1];

                        if (timeParser.boolInitialTime()) {
                            FileOutput.writeToFile("SUCCESS: Time has been set to " + timeParser.strSetInitialTime() + "!");
                            currentTime = timeParser.getLocalDateTime(strTime);
                            initialTime = currentTime;
                            countInitialTime ++ ;
                        } else {
                            FileOutput.writeToFile("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                            break;
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Erroneous command!"); // if we see outside of the first line
                    }

                } else {
                    FileOutput.writeToFile("ERROR: First command must be set initial time! Program is going to terminate!");
                    break;
                }


            } else if (containsSetTime(commandLine)) {
                String strSetTime = removedTab[1];

                if (timeParser.boolTime()) {
                    if (currentTime.isAfter(timeParser.getLocalDateTime(strSetTime))) {
                        FileOutput.writeToFile("ERROR: Time cannot be reversed!");

                    } else if (currentTime.isEqual(timeParser.getLocalDateTime(strSetTime))) {
                        FileOutput.writeToFile("ERROR: There is nothing to change!");

                    } else {
                        currentTime = timeParser.getLocalDateTime(strSetTime);
                    }
                } else {
                    FileOutput.writeToFile("ERROR: Time format is not correct!");
                }


            } else if (containsSkipMinutes(commandLine)) {

                    if (boolLengthRemovedTab(removedTab, 2)) {
                        if (timeParser.boolSkipMinutes()) {
                            currentTime = currentTime.plusMinutes(timeParser.getSkipMinutes());
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Erroneous command!");
                    }


            } else if (containsNop(commandLine)) {

                if (boolLengthRemovedTab(removedTab,1)) {
                    int nullPointer = 0;
                    for (String[] line : allDevicesList) {
                        if (line[(line.length) - 1] != null) {
                            nullPointer ++ ;
                            switchTimes.add(line[(line.length) - 1]);
                        }
                    } if (nullPointer == 0) {
                        FileOutput.writeToFile("ERROR: There is nothing to switch!");
                    } else {
                        smallestSwitchTime = "3000-03-31_15:00:00";
                        for (String eachSwitchTime: switchTimes) {

                            if (timeParser.getLocalDateTime(eachSwitchTime).isBefore(timeParser.getLocalDateTime(smallestSwitchTime)) &&
                                    timeParser.getLocalDateTime(eachSwitchTime).isAfter(currentTime)) {
                                smallestSwitchTime = eachSwitchTime;

                                currentTime = timeParser.getLocalDateTime(smallestSwitchTime);

                            }
                        }
                    }

                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }


            } else if (containsAdd(commandLine)) {


                SmartDevices smartDevices = new SmartDevices(removedTab[1],removedTab[2],
                        removedTab, addNames, allDevicesList);

                switch (smartDevices.getDevice()) {
                    case ("SmartPlug"):
                        if (removedTab.length != 5 && removedTab.length != 4 && removedTab.length != 3) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }
                        if (removedTab.length == 5) {
                            SmartPlug smartPlug = new SmartPlug
                                    (removedTab[1],removedTab[2], removedTab[3], removedTab[4],
                                            removedTab, addNames, allDevicesList) ;

                        }
                        else if (removedTab.length == 4) {
                            SmartPlug smartPlug = new SmartPlug(removedTab[1],removedTab[2], removedTab[3],
                                    removedTab, addNames, allDevicesList);
                        }
                        else if (removedTab.length == 3) {
                            SmartPlug smartPlug = new SmartPlug(removedTab[1],removedTab[2],
                                    removedTab, addNames, allDevicesList);
                        }
                        break;

                    case ("SmartCamera"):
                        if (removedTab.length != 5 && removedTab.length != 4) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }
                        else if (removedTab.length == 5) {
                            SmartCamera smartCamera = new SmartCamera
                                    (removedTab[1],removedTab[2], removedTab[3], removedTab[4],
                                            removedTab, addNames, allDevicesList);
                        }
                        else if (removedTab.length == 4) {
                            SmartCamera smartCamera = new SmartCamera
                                    (removedTab[1],removedTab[2], removedTab[3],
                                            removedTab, addNames, allDevicesList);
                        }
                        break;

                    case ("SmartLamp"):
                        if (removedTab.length != 3 && removedTab.length != 4 && removedTab.length != 6) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }
                        else if (removedTab.length == 6) {
                            SmartLamp smartLamp = new SmartLamp
                                    (removedTab[1],removedTab[2], removedTab[3],
                                            removedTab[4], removedTab[5], removedTab, addNames, allDevicesList);
                        }
                        else if (removedTab.length == 4) {
                            SmartLamp smartLamp = new SmartLamp
                                    (removedTab[1],removedTab[2], removedTab[3], removedTab, addNames, allDevicesList);
                        }
                        else if (removedTab.length == 3) {
                            SmartLamp smartLamp = new SmartLamp(removedTab[1],removedTab[2],
                                    removedTab, addNames, allDevicesList);
                        }
                        break;

                    case ("SmartColorLamp"):
                        if (removedTab.length != 3 && removedTab.length != 4 && removedTab.length != 6) {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }
                        else if (removedTab.length == 6) {
                            if (!removedTab[4].startsWith("0x")) {
                                SmartLamp smartLamp = new SmartLamp
                                        (removedTab[1],removedTab[2], removedTab[3],
                                                removedTab[4], removedTab[5], removedTab, addNames, allDevicesList);
                            }
                            else if (removedTab[4].startsWith("0x")) {
                                SmartColorLamp smartColorLamp = new SmartColorLamp
                                        (removedTab[1], removedTab[2], removedTab[3],
                                                removedTab[4], removedTab[5], removedTab, addNames, allDevicesList);
                            }
                        }

                        else if (removedTab.length == 4) {
                            SmartLamp smartLamp = new SmartLamp
                                    (removedTab[1],removedTab[2], removedTab[3], removedTab, addNames, allDevicesList);
                        }

                        else if (removedTab.length == 3) {
                            SmartLamp smartLamp = new SmartLamp(removedTab[1],removedTab[2],
                                    removedTab, addNames, allDevicesList);
                        }
                        break;
                }


            } else if (containsRemove(commandLine)) {

                String nameWillRemove = removedTab[1];
                if(boolLengthRemovedTab(removedTab, 2)) {
                    if (addNames.contains(nameWillRemove)) {
                        addNames.remove(nameWillRemove);
                        for (String[] line : allDevicesList) {
                            if (line[1].equals(nameWillRemove)) {
                                int lineIndex = allDevicesList.indexOf(line);
                                for (int findOn = 0; findOn< (line.length)-1; findOn++) {
                                    if (line[findOn].equals("On")) {
                                        allDevicesList.get(lineIndex)[findOn] = "Off";
                                    }
                                }
                                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
                                String strCurrentTime = currentTime.format(formatter);

                                LocalDateTime newCurrentTime = LocalDateTime.of(Integer.parseInt(strCurrentTime.substring(0, 4)),
                                        Integer.parseInt(strCurrentTime.substring(5, 7)),
                                        Integer.parseInt(strCurrentTime.substring(8, 10)),
                                        Integer.parseInt(strCurrentTime.substring(11, 13)),
                                        Integer.parseInt(strCurrentTime.substring(14, 16)));

                                String strInitialTime = initialTime.format(formatter);
                                LocalDateTime newInitialTime = LocalDateTime.of(Integer.parseInt(strInitialTime.substring(0, 4)),
                                        Integer.parseInt(strInitialTime.substring(5, 7)),
                                        Integer.parseInt(strInitialTime.substring(8, 10)),
                                        Integer.parseInt(strInitialTime.substring(11, 13)),
                                        Integer.parseInt(strInitialTime.substring(14, 16)));

                                                                                    // get the minutes difference
                                double minutesDifference = ChronoUnit.MINUTES.between(newInitialTime, newCurrentTime);
                                double hourFormat = (minutesDifference / 60) ;


                                allDevicesList.remove(lineIndex);
                                addNames.remove(nameWillRemove);

                                FileOutput.writeToFile
                                        ("SUCCESS: Information about removed smart device is as follows:");

                                switch (line[0]) {
                                    case ("SmartColorLamp"):

                                        FileOutput.writeToFile(String.format("%s %s is %s and its color value is %sK with %s%% brightness, " +
                                                        "and its time to switch its status is %s.",
                                                "Smart Color Lamp", line[1], line[2].toLowerCase(), line[3], line[4],
                                                line[5]));
                                        break;
                                    case ("SmartLamp"):
                                        FileOutput.writeToFile(String.format("%s %s is %s and its kelvin value is %sK " +
                                                        "with %s%% brightness, and its time to switch its status is %s.",
                                                "Smart Lamp", line[1], line[2].toLowerCase(), line[3], line[4], line[5]));
                                        break;

                                    case ("SmartCamera"):
                                        double megaByteConsumption = Double.parseDouble(line[2]) * minutesDifference;
                                        FileOutput.writeToFile(String.format("%s %s is %s and used %s MB of storage " +
                                                        "so far (excluding current status), " +
                                                        "and its time to switch its status is %s.",
                                                "Smart Camera", line[1], line[3].toLowerCase(),
                                                convertDecimalFormat(megaByteConsumption), line[4]));
                                        break;

                                    case ("SmartPlug"):
                                        double wattConsumption = Double.parseDouble(line[3]) * hourFormat * 220;
                                        FileOutput.writeToFile(String.format("%s %s is %s and consumed %sW so far " +
                                                        "(excluding current device), and its time to switch its status " +
                                                        "is %s.",
                                                "Smart Plug", line[1], line[2].toLowerCase(),
                                               convertDecimalFormat(wattConsumption), line[4]));
                                        break;
                                }
                                break;
                            }
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: There is not such a device!"); // not contains device name
                    }

                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }



            } else if (containsSetSwitchTime(commandLine)) {
                String name = removedTab[1];
                String strSwitchTime = removedTab[2];

                if(boolLengthRemovedTab(removedTab, 3)) {
                    if (addNames.contains(name)) {
                        if (timeParser.boolSwitchTime()) {
                            if (timeParser.getLocalDateTime(strSwitchTime).isBefore(currentTime)) {
                                FileOutput.writeToFile("ERROR: Switch time cannot be in the past!");
                            } else {
                                for (String [] line : allDevicesList) {
                                    if (line[1].equals(name)) {

                                        line[(line.length)-1] = strSwitchTime;
                                    }
                                }
                            }
                        } else {
                            FileOutput.writeToFile("ERROR: Time format is not correct!");
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: There is not such a device!");
                    }
                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }




            } else if (containsSwitch(commandLine)) {
                String name = removedTab[1];
                String status = removedTab[2];

                if(boolLengthRemovedTab(removedTab, 3)) {
                    if (addNames.contains(name)) {
                        for (String[] line : allDevicesList) {
                            if (line[1].equals(name)) {
                                if (status.equals("On") || status.equals("Off")) {
                                    if (status.equals("On")) {
                                        if (line[2].equals("On")) {
                                            FileOutput.writeToFile("ERROR: This device is already switched on!");
                                        } else  {
                                            line[2] = "On";
                                        }
                                    } else if (status.equals("Off")) {
                                        if (line[2].equals("Off")) {
                                            FileOutput.writeToFile("ERROR: This device is already switched off!");
                                        } else {
                                            line[2] = "Off";
                                        }
                                    }
                                } else {
                                    FileOutput.writeToFile("ERROR: Erroneous command!");
                                }
                            }
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: There is not such a device!");
                    }

                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }


            } else if (containsChangeName(commandLine)) {

                if(boolLengthRemovedTab(removedTab, 3)) {
                    String oldName = removedTab[1];
                    String newName = removedTab[2];
                     if (!oldName.equals(newName)){
                        if (addNames.contains(oldName))  {
                            if (!addNames.contains(newName)) {
                                int oldNameIndex = addNames.indexOf(oldName);
                                addNames.set(oldNameIndex, newName);
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(oldName)) {
                                        int lineIndex = allDevicesList.indexOf(line);
                                        allDevicesList.get(lineIndex)[1] = newName;
                                    }
                                }
                            } else {
                                FileOutput.writeToFile("ERROR: There is already a smart device with same name!");
                            }
                        } else {
                            FileOutput.writeToFile("ERROR: There is not such a device!");
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: Both of the names are the same, nothing changed!");
                    }
                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }


            } else if (containsPlugIn(commandLine)) {
                String name = removedTab[1];
                String strAmpere = removedTab[2];

                if(boolLengthRemovedTab(removedTab, 3)) {
                    if (addNames.contains(name)) {
                        for (String[] line : allDevicesList) {
                            if (line[1].equals(name)) {
                                if (line[0].equals("SmartPlug")) {
                                    try {
                                        double ampere = Double.parseDouble(strAmpere);
                                        if (ampere <= 0) {
                                            FileOutput.writeToFile("ERROR: Ampere value must be a positive number!");
                                        } else {
                                            if (line[3] == null) {
                                                line[3] = strAmpere;

                                            } else {
                                                FileOutput.writeToFile
                                                        ("ERROR: There is already an item plugged in to that plug!");
                                            }
                                        }
                                    }  catch (NumberFormatException e) {
                                        FileOutput.writeToFile("ERROR: Erroneous command!");
                                    }
                                } else {
                                    FileOutput.writeToFile("ERROR: This device is not a smart plug!");
                                }
                            }
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: There is not such a device!");
                    }

                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }



            } else if (containsPlugOut(commandLine)) {
                String name = removedTab[1];

                if(boolLengthRemovedTab(removedTab, 2)) {
                    if (addNames.contains(name)) {
                        for (String[] line : allDevicesList) {
                            if (line[1].equals(name)) {
                                if (line[0].equals("SmartPlug")) {
                                    if (line[3] != null) {
                                        line[3] = null;
                                    } else {
                                        FileOutput.writeToFile("ERROR: This plug has no item to plug out from that plug!");
                                    }
                                } else {
                                    FileOutput.writeToFile("ERROR: This device is not a smart plug!");
                                }
                            }
                        }
                    } else {
                        FileOutput.writeToFile("ERROR: There is not such a device!");
                    }
                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }


            } else if (containsSet(commandLine)) {

                String name = removedTab[1];
                String commandType = removedTab[0];
                String strKelvin = removedTab[2];
                String strBrightness;
                String strColorCode = removedTab[2];

                switch (commandType) {
                    case "SetKelvin":
                        if(boolLengthRemovedTab(removedTab, 3)) {
                            if (addNames.contains(name)) {
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(name)) {
                                        if (line[0].equals("SmartLamp")) {
                                            try {
                                                int kelvin = Integer.parseInt(strKelvin);
                                                if (kelvin >= 2000 && kelvin <= 6500) {
                                                    line[3] = strKelvin;
                                                } else {
                                                    FileOutput.writeToFile
                                                            ("ERROR: Kelvin value must be in range of 2000K-6500K!");
                                                }
                                            }  catch (NumberFormatException e) {
                                                FileOutput.writeToFile("ERROR: Erroneous command!");
                                            }
                                        } else {
                                            FileOutput.writeToFile("ERROR: This device is not a smart lamp!");
                                        }
                                    }
                                }
                                break;
                            } else {
                                FileOutput.writeToFile("ERROR: There is not such a device!");
                            }
                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }


                    case "SetBrightness":
                        if(boolLengthRemovedTab(removedTab, 3)) {
                            if (addNames.contains(name)) {
                                strBrightness = removedTab[2];
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(name)) {
                                        if (line[0].equals("SmartLamp")) {
                                            try {
                                                int brightness = Integer.parseInt(strBrightness);
                                                if (brightness >= 0 && brightness <= 100) {
                                                    line[4] = strBrightness;
                                                } else {
                                                    FileOutput.writeToFile
                                                            ("ERROR: Brightness must be in range of 0%-100%!");
                                                }
                                            }  catch (NumberFormatException e) {
                                                FileOutput.writeToFile("ERROR: Erroneous command!");
                                            }
                                        } else {
                                            FileOutput.writeToFile("ERROR: This device is not a smart lamp!");
                                        }
                                    }
                                }
                                break;

                            } else {
                                FileOutput.writeToFile("ERROR: There is not such a device!");
                            }

                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }


                    case "SetColorCode":
                        if(boolLengthRemovedTab(removedTab, 3)) {
                            if (addNames.contains(name)) {
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(name)) {
                                        if (line[0].equals("SmartColorLamp")) {
                                            try {
                                                int ColorCode = Integer.decode(strColorCode);
                                                if (ColorCode <= 0xFFFFFF && ColorCode >= 0x000000) {
                                                    line[3] = strColorCode;

                                                } else {
                                                    FileOutput.writeToFile
                                                            ("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                                                }

                                            } catch (NumberFormatException e) {
                                                FileOutput.writeToFile("ERROR: Erroneous command!");
                                            }
                                        } else {
                                            FileOutput.writeToFile("ERROR: This device is not a smart color lamp!");
                                        }
                                    }
                                }
                                break;
                            } else {
                                FileOutput.writeToFile("ERROR: There is not such a device!");
                            }

                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }




                    case "SetWhite":
                        if(boolLengthRemovedTab(removedTab, 4)) {
                            if (addNames.contains(name)) {
                                strBrightness = removedTab[3];
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(name)) {
                                        if (line[0].equals("SmartColorLamp") || line[0].equals("SmartLamp")) {
                                            try{
                                                int kelvin = Integer.parseInt(strKelvin);
                                                if (kelvin >= 2000 && kelvin <= 6500) {
                                                    try {
                                                        int brightness = Integer.parseInt(strBrightness);
                                                        if (brightness >= 0 && brightness <= 100) {
                                                            line[3] = strKelvin;
                                                            line[4] = strBrightness;
                                                        } else {
                                                            FileOutput.writeToFile
                                                                    ("ERROR: Brightness must be in range of 0%-100%!");
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        FileOutput.writeToFile("ERROR: Erroneous command!");
                                                    }
                                                } else{
                                                    FileOutput.writeToFile
                                                            ("ERROR: Kelvin value must be in range of 2000K-6500K!");
                                                }
                                            } catch (NumberFormatException e) {
                                                FileOutput.writeToFile("ERROR: Erroneous command!");
                                            }
                                        } else {
                                            FileOutput.writeToFile("ERROR: This device is not a smart lamp!");
                                        }
                                    }
                                }
                                break;
                            } else {
                                FileOutput.writeToFile("ERROR: There is not such a device!");
                            }

                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }





                    case "SetColor":
                        if(boolLengthRemovedTab(removedTab, 4)) {
                            if (addNames.contains(name)) {
                                strBrightness = removedTab[3];
                                for (String[] line : allDevicesList) {
                                    if (line[1].equals(name)) {
                                        if (line[0].equals("SmartColorLamp")) {
                                            try {
                                                int ColorCode = Integer.decode(strColorCode);
                                                if (ColorCode <= 0xFFFFFF && ColorCode >= 0x000000) {
                                                    line[3] = strColorCode;
                                                    try {
                                                        int brightness = Integer.parseInt(strBrightness);
                                                        if (brightness >= 0 && brightness <= 100) {
                                                            line[4] = strBrightness;
                                                        } else {
                                                            FileOutput.writeToFile
                                                                    ("ERROR: Brightness must be in range of 0%-100%!");
                                                        }
                                                    }  catch (NumberFormatException e) {
                                                        FileOutput.writeToFile("ERROR: Erroneous command!");
                                                    }
                                                } else {
                                                    FileOutput.writeToFile
                                                            ("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                                                }
                                            } catch (NumberFormatException e) {
                                                FileOutput.writeToFile("ERROR: Erroneous command!");
                                            }
                                        } else {
                                            FileOutput.writeToFile("ERROR: This device is not a smart color lamp!");
                                        }
                                    }
                                }
                                break;
                            } else {
                                FileOutput.writeToFile("ERROR: There is not such a device!");
                            }

                        } else {
                            FileOutput.writeToFile("ERROR: Erroneous command!");
                        }
                default:
                    FileOutput.writeToFile("ERROR: Erroneous command!");
            }


            } else if (containsZReport(commandLine)) {
                if (boolLengthRemovedTab(removedTab, 1)) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
                    strNopSwitchedTime = currentTime.format(formatter);
                    FileOutput.writeToFile("Time is:\t" + strNopSwitchedTime);


                } else {
                    FileOutput.writeToFile("ERROR: Erroneous command!");
                }

            }


            else {
                FileOutput.writeToFile("ERROR: Erroneous command!");
            }
            if (countInitialTime != 0) {
                if (! (inputFile[(inputFile.length) - 1].contains("ZReport")) ) {
                    FileOutput.writeToFile("ZReport:");
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
                    strNopSwitchedTime = currentTime.format(formatter);
                    FileOutput.writeToFile("Time is:\t" + strNopSwitchedTime);

                }

            }
        }


        FileOutput.close();


    }


    // Command checking methods
    private boolean containsSetInitialTime(String element) {
        return element.contains("SetInitialTime");
    }

    private boolean containsSetTime(String element) {
        return element.contains("SetTime");
    }

    private boolean containsSkipMinutes(String element) {
        return element.contains("SkipMinutes");
    }

    private boolean containsNop(String element) {
        return element.contains("Nop");
    }

    private boolean containsAdd(String element) {
        return element.contains("Add");
    }

    private boolean containsRemove(String element) {
        return element.contains("Remove");
    }

    private boolean containsSetSwitchTime(String element) {
        return element.contains("SetSwitchTime");
    }

    private boolean containsSwitch(String element) {
        return element.contains("Switch");
    }

    private boolean containsChangeName(String element) {
        return element.contains("ChangeName");
    }

    private boolean containsPlugIn(String element) {
        return element.contains("PlugIn");
    }

    private boolean containsPlugOut(String element) {
        return element.contains("PlugOut");
    }

    private boolean containsSet(String element) {
        return element.contains("Set");
    }

    private boolean containsZReport(String element) {
        return element.contains("ZReport");
    }

    private boolean boolLengthRemovedTab(String[] removedTab, int arrayLength) {
        return removedTab.length == arrayLength;
    }


    private String convertDecimalFormat(double doubleNumber) {

        double number = doubleNumber;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }

}





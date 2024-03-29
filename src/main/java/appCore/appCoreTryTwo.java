package appCore;

import dropBoxPackage.DropBoxCore;
import interfacePac.SpecInterface;
import localStoragePackage.LocalStorageCore;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class appCoreTryTwo {

    private SpecInterface dropBoxObj;

    public static void main(String[] args) {
        new appCoreTryTwo().runApp();
    }

    private void runApp(){
        //
        dropBoxObj = new LocalStorageCore();
        //dropBoxObj = new DropBoxCore();
        //
        Scanner s = new Scanner(System.in);
        String currPath;
        String username;
        String password;
        do{
            System.out.println("Enter root file name.");
            currPath = s.nextLine();
        }while (currPath.isEmpty());

        do{
            System.out.println("Success. Now enter username.");
            username = s.nextLine();
        }while (username.length() == 0 || username.length() > 32);

        do{
            System.out.println("Now enter password.");
            password = s.nextLine();
        }while (!(password.length() < 32));

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            password = "";
            for(int i = 0; i < hash.length; i++){
                password += (char)hash[i];
            }
            password = password.replaceAll("\\n", "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(dropBoxObj.logIn(username, password, currPath) == -1) return;


        while(true){
            System.out.println("Enter 'help' to list all comands.");

            String consoleRead;

            consoleRead = s.nextLine();

            if(consoleRead.toLowerCase().equals("help")) {
                System.out.println("1 - Logout and close  \n2 - Make new file  \n3 - Delete file  " +                          // Lists all functions
                        "\n4 - Add new user\n5 - Change user permission" +
                        "\n6 - Search file  \n7 - Zip and upload \n8 - Download files as zip  "+
                        "\n9 - Manage forbidden extensions   + \n10 - Rename file" + "\n11 - Manage metadata" );
            }
            if(consoleRead.equals("1")) {
                dropBoxObj.logOut();    // Basically stops the program and deletes the local copy if file
                break;
            }
            if(consoleRead.equals("2")){

                while(true) {
                    try {
                        System.out.println("Enter the name of the file. NOTE IF you wish to make multiple files enter: (10) fileName . This will make 10 files [fileName0,...,fileName10].");
                        consoleRead = s.nextLine();
                        dropBoxObj.addFile(consoleRead, true);
                        break;
                    }catch (NumberFormatException e){
                        System.out.println("Invalid interval annotation");
                        continue;
                    }
                }

            }
            if(consoleRead.equals("3")){
                System.out.println("Enter name of the file you wish to delete.");
                consoleRead = s.nextLine();

                dropBoxObj.removeFile(consoleRead);

            }
            if(consoleRead.equals("4")) {
                System.out.println("Enter new username for new user.");
                consoleRead = s.nextLine();
                while(!dropBoxObj.createUser(consoleRead)){                                    // Loops until valid username is entered
                    System.out.println("User already exists, please choose another username.");
                    consoleRead = s.nextLine();
                }
            }
            if(consoleRead.equals("5")){
                System.out.println("Enter username of user whose credentials you want to change.");
                consoleRead = s.nextLine();
                dropBoxObj.changeUserCredentials(consoleRead);
            }
            if(consoleRead.equals("6")){
                System.out.println("Enter the name of the file");

                consoleRead = s.nextLine();

                dropBoxObj.searchFiles(consoleRead);
            }
            if(consoleRead.equals("7")){
                System.out.println("Enter the names of the files you want zipped and uploaded: \"file1\" \"file2\"... ");

                consoleRead = s.nextLine();


               dropBoxObj.uploadZIP(consoleRead);
            }
            if(consoleRead.equals("8")){
                System.out.println("Enter the names of files you want zipped i the next format: file1,file2,file3,...,fileN");

                consoleRead = s.nextLine();

                dropBoxObj.downloadZIP(consoleRead, "");
            }
            if(consoleRead.equals("9")){
                System.out.println("Enter extension to be forbidden");

                consoleRead = s.nextLine();

                dropBoxObj.forbidExtension(consoleRead);
            }
            if(consoleRead.equals("10")){
                System.out.println("Enter file you want to rename");
                String tmp;
                consoleRead = s.nextLine();
                System.out.println("Enter new file name");
                tmp = s.nextLine();
                dropBoxObj.rename(consoleRead, tmp);
            }
            if(consoleRead.equals("11")){

                System.out.println("Do you want to load metadata from json file or no? <Yes/No>");
                consoleRead = s.nextLine();
                boolean yesno = true;
                while(yesno){

                    if(consoleRead.toLowerCase().equals("yes")){
                        System.out.println("Enter the name of the file");
                        consoleRead = s.nextLine();

                        dropBoxObj.setMetadata("-f "+ consoleRead);


                        yesno = false;
                    }
                    else if(consoleRead.toLowerCase().equals("no")){
                        System.out.println("Enter the key value: ");
                        consoleRead = s.nextLine();
                        String keyValue = consoleRead;
                        keyValue += ":";

                        System.out.println("Enter the value of value");

                        consoleRead = s.nextLine();
                        keyValue += consoleRead;
                        dropBoxObj.setMetadata(keyValue);

                        yesno = false;
                    }
                    else{
                        continue;
                    }
                }
            }
        }
    }
}

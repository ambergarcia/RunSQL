import javax.swing.*;

/*
 * Written by: Amber Garcia
 * public class Main
 * Creates the JTable and initializes the admin for running queries.
 * */


public class Main {

    // Set to the correct UserId.
    static String title = "Project 3";
    static int UserId = 0;
    static String defaultDataBase = "master";

    public static void main(String[] args) {
	// write your code here
        RunSQL admin = setAdmin(UserId, defaultDataBase);
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                Presentation ui = new Presentation(title, admin, null);
                JPanel root = ui.getRootPanel();
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
                frame.setContentPane(root);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

    }
    // We create the default
    public static RunSQL setAdmin(int GroupMemberId, String database){

        String[] defaultURL = {"jdbc:sqlserver", "localhost:12001", "master", null, null};
        RunSQL a = new RunSQL(defaultURL[0], defaultURL[1], defaultURL[2], defaultURL[3], defaultURL[4]);
        a.switchDatabase(database);
        a.setGroupMemberId(GroupMemberId);

        return a;
    }
}

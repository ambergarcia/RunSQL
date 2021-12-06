import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Written by: Amber Garcia
 * public class Presentation
 * For the Presentation.form that displays the JTable, and allows queries to be ran through it.
 * */

public class Presentation {
    RunSQL admin;
    private JPanel rootPanel;
    private JTable queryTable;
    private JLabel QueryLabel;
    private JTextPane QueryPane;
    private JButton runQueryButton;
    private JTextPane Messages;
    private JButton connectToServerButton;
    private JTextField userNameTextField;
    private JTextField serverTextField;
    private JPasswordField passwordTextField;

    public Presentation(String label, RunSQL admin, ListTableModel model) {
        this.admin = admin;
        QueryLabel.setText(label);
        createTable();
        runQueryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                admin.run_query(QueryPane.getText());
                if (admin.getModel() != null) {
                    queryTable.setModel(admin.getModel());
                    resizeColumns();
                }
                Messages.setText(admin.getRunSQLResponse());
            }
        });

        connectToServerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String user = userNameTextField.getText();
                String password = String.valueOf(passwordTextField.getPassword());
                String server = serverTextField.getText();
                if (userNameTextField.getText().isBlank() || passwordTextField.getPassword() == null || userNameTextField.getText().isBlank()) {
                    Messages.setText("Enter valid username / password / server.");
                } else {
                    if (server.contains("localhost")) {
                        server = server.replace(',', ':');
                    }
                    ConnectDatabase cd = new ConnectDatabase(admin.getConnectionUrl().getBeginning(), server, "master", user, password);
                    admin.setConnectionUrl(cd);
                    Messages.setText("Connection URL modified successfully.");
                }
            }
        });
    }

    public void resizeColumns() {
        // Recieved from: https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/
        // Handles increasing the column length for both the title and contents s.t its readable.
        // Slight adjustment to preferredWidth to properly adjust for the title of a column.

        for (int column = 0; column < queryTable.getColumnCount(); column++) {
            DefaultTableCellRenderer align = new DefaultTableCellRenderer();
            align.setHorizontalAlignment(JLabel.CENTER);
            queryTable.getColumnModel().getColumn(column).setCellRenderer(align);

            TableColumn tableColumn = queryTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth() + queryTable.getColumnName(column).length() * 10;
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < queryTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = queryTable.getCellRenderer(row, column);
                Component c = queryTable.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + queryTable.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void createTable() {
        //create an array of objects for each row in each column of table
        // Array of rows, then for each row an array of cells.
        Object[][] data = {{}, {}, {}};
        queryTable.setModel(new DefaultTableModel(

                null,
                new String[]{"column1", "column2", "column3"}

        ));
    }

    public void setTableAndLabel(String label, String[] info, Object[][] data) {
        QueryLabel.setText(label);
        queryTable.setModel(new DefaultTableModel(
                data,
                info
        ));
    }

}
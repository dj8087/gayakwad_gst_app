/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ajgold.balanceSheetDetails;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author admin
 */
public class MyDefaultTableCellRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(row== table.getRowCount()-1 ? Color.LIGHT_GRAY : Color.WHITE);
        return c;
    }

}

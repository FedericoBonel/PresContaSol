package vista.componentes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Componente de una JTable que no permite edicion y se auto ordena como el usuario requiera
 */
public class JTableNoEditable extends JTable {

    /**
     * Nombres de las columnas
     */
    private String[] columnas;

    /**
     * Constructor SIN DATOS que realiza lo mismo que el del super
     *
     * @param columnas Nombres de las columnas a mostrar en orden
     */
    public JTableNoEditable(String[] columnas){
        super(new String[][]{}, columnas);
        this.columnas = columnas;
        super.getTableHeader().setReorderingAllowed(false);
        super.setAutoCreateRowSorter(true);
    }

    /**
     * Devuelve siempre falso evitando edicion en la tabla
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Actualiza los datos de la tabla con unos nuevos bajo las mismas columnas
     * @param datos Datos a poner en la tabla actualizada
     */
    public void actualizarCon(String[][] datos){
        super.setModel(new DefaultTableModel(datos, columnas));
    }

}

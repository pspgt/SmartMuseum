/**
 * Questa classe si divide in due parti: la prima parte invia una chiamata rest
 * al server per scaricare le preferenze in base all'ID dell'utente; la seconda
 * invece genera la tabella
 */
package smartmuseumtable;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTable;

/*
 * @authors Cosimo Antonaci & Gabriele Tramonte
 */
public class Museo extends javax.swing.JFrame {

    private Vector<String> n_Opera;
    private Vector<String> nomeAutore;
    private Vector<String> cognomeAutore;
    private Vector<Integer> id_Opere;
    private JPanel descrizione;
    private JPanel immagine;

    public Museo() {
        int id = Utente.getIstance().getId();
        RestClient rest = new RestClient("Preferenze", id);
        while (!rest.isStatus()) {

        }
        String restResult = rest.getOutput();

        n_Opera = new Vector<String>();
        nomeAutore = new Vector<String>();
        cognomeAutore = new Vector<String>();
        id_Opere = new Vector<Integer>();
        if (restResult.equals("No record")) {

        } else {
            String[] tmp = restResult.split("\n");
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].contains("NomeOpera")) {
                    n_Opera.addElement(tmp[i].substring(tmp[i].indexOf(" ") + 1));
                } else if (tmp[i].contains("Nome ")) {
                    nomeAutore.addElement(tmp[i].substring(tmp[i].indexOf(" ") + 1));
                } else if (tmp[i].contains("Cognome")) {
                    cognomeAutore.addElement(tmp[i].substring(tmp[i].indexOf(" ") + 1));
                } else if (tmp[i].contains("idOpera")) {
                    id_Opere.addElement(Integer.parseInt(tmp[i].substring(tmp[i].indexOf(" ") + 1)));
                }

            }
        }
        initComponents();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();

        this.addWindowListener(new MuseoWindowListener());

        setTitle("Smart Museum");
        int larghezza = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int altezza = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setBounds(new java.awt.Rectangle(0, 0, larghezza, altezza));
        setResizable(false);
        jButton1.setFont(new java.awt.Font("Arial", 0, 24));
        jButton1.setText("Logout");
        jButton1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Utente.getIstance().logout();
                setVisible(false);
            }
        });
        //generazioe tabella
        Vector<String> colonne = new Vector<String>();
        colonne.addElement("Identificativo Opera");
        colonne.addElement("Nome Dell'Opera");
        colonne.addElement("Nome Autore");
        colonne.addElement("Cognome Autore");
        Vector<Vector> righe = new Vector<Vector>();
        if (n_Opera.size() == 0) {
            Vector<String> row = new Vector<String>();
            row.addElement("0");
            row.addElement("Non");
            row.addElement("ci sono");
            row.addElement("Preferenze al momento");
            righe.addElement(row);
        } else {
            for (int i = 0; i < n_Opera.size(); i++) {
                Vector<String> row = new Vector<String>();
                row.addElement(Integer.toString(id_Opere.elementAt(i)));
                row.addElement(n_Opera.elementAt(i));
                row.addElement(nomeAutore.elementAt(i));
                row.addElement(cognomeAutore.elementAt(i));
                righe.addElement(row);
            }
        }
        jTable1 = new JTable(righe, colonne);
        jTable1.getSelectionModel().addListSelectionListener(new TableListener(this, jTable1));
        jScrollPane1.setViewportView(jTable1);
        immagine = new JPanel();
        descrizione = new JPanel();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(jScrollPane1, BorderLayout.NORTH);
        this.getContentPane().add(immagine, BorderLayout.EAST);
        this.getContentPane().add(descrizione, BorderLayout.WEST);
        JPanel south_p = new JPanel();
        south_p.setLayout(new BorderLayout());
        this.getContentPane().add(south_p, BorderLayout.SOUTH);
        south_p.add(jButton1, BorderLayout.CENTER);
    }// </editor-fold>                        

    public JPanel getDescrizione() {
        return descrizione;
    }

    public JPanel getImmagine() {
        return immagine;
    }
    // dichiarazione variabili                   
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public static String token;
    // fine dichiarazione variabili                  
}

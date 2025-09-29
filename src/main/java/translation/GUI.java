package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


public class GUI {

    public static String country = "de";
    public static String language = "de";
    public static String result;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator("sample.json");
            CountryCodeConverter  countryCodeConverter = new CountryCodeConverter();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel row3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel languagePanel = new JPanel();
            languagePanel.setLayout(new BoxLayout(languagePanel, BoxLayout.Y_AXIS));
            final JComboBox<String> languageComboBox = new JComboBox();
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            String[] items = new String[translator.getCountryCodes().size()];
            final JList<String> list = new JList(items);
            list.setSelectionMode(2);
            row1Panel.add(new JLabel("Language:"));
            row1Panel.add(languageComboBox);
            row2Panel.add(new JLabel("Translation:"));
            row2Panel.add(resultLabel);
            JScrollPane scrollPane = new JScrollPane(list);
            row3Panel.add(scrollPane);
            languagePanel.add(row1Panel);
            languagePanel.add(row2Panel);
            languagePanel.add(row3Panel);



            for(String languageCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }

            int i = 0;

            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = countryCodeConverter.fromCountryCode(countryCode);
            }

            languageComboBox.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == 1) {
                        language = languageCodeConverter.fromLanguage(languageComboBox.getSelectedItem().toString());
                        result = translator.translate(country, language);
                        if (result == null) {
                            result = "no translation found!";
                            System.out.println(country + " " + language + " " + result);
                        }
                    }
                    resultLabel.setText(result);

                }
            });


            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    int[] indices = list.getSelectedIndices();
                    String[] items = new String[indices.length];

                    for(int i = 0; i < indices.length; ++i) {
                        items[i] = (String)list.getModel().getElementAt(indices[i]);
                    }
                    String arrayString = Arrays.toString(items);
                    country = countryCodeConverter.fromCountry(arrayString.substring(1, arrayString.length()-1));
                    result = translator.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }
            });

            JFrame frame = new JFrame("GUI Frame");
            frame.setContentPane(languagePanel);
            frame.setDefaultCloseOperation(3);
            frame.setLocationRelativeTo((Component)null);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

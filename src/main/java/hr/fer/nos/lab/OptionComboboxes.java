package hr.fer.nos.lab;

import com.vaadin.ui.ComboBox;

/**
 * <p>Created: 2018-05-21 1:19:48 PM</p>
 *
 * @author marin
 */
public class OptionComboboxes {

    public static ComboBox getMethodsComboBox() {
        final ComboBox box = new ComboBox("Algoritam");

        box.setNullSelectionAllowed(false);

        box.addItem("AES");
        box.setItemCaption("AES", "AES");
        box.addItem("DESede");
        box.setItemCaption("DESede", "3DES");
        box.select("AES");
        return box;
    }

    public static ComboBox getCryptWayComboBox() {
        final ComboBox box = new ComboBox("Način kriptiranja");

        box.setNullSelectionAllowed(false);

        box.addItem("CBC");
        box.addItem("ECB");
        box.addItem("CTR");
        box.select("CBC");
        return box;
    }

    public static ComboBox getKeySizeComboBox() {
        final ComboBox box = new ComboBox("Veličina ključa");

        box.setNullSelectionAllowed(false);

        box.addItem(112);
        box.addItem(128);
        box.addItem(168);
        box.addItem(256);
        box.select(128);
        return box;
    }

    public static ComboBox getAMethodsComboBox() {
        final ComboBox box = new ComboBox("Algoritam");

        box.setNullSelectionAllowed(false);
        box.addItem("RSA");
        box.addItem("ElGamal");
        box.select("RSA");
        return box;
    }

    public static ComboBox getAHashComboBox() {
        final ComboBox box = new ComboBox("Hash funkcija");

        box.setNullSelectionAllowed(false);

        box.addItem("SHA-256");
        box.addItem("SHA-384");
        box.addItem("SHA-512");
        box.addItem("SHA3-256");
        box.addItem("SHA3-384");
        box.addItem("SHA3-512");
        box.select("SHA-256");

        return box;
    }

    public static ComboBox getAKeySizeComboBox() {
        final ComboBox box = new ComboBox("Veličina ključa");

        box.setNullSelectionAllowed(false);

        box.addItem(512);
        box.addItem(1024);
        box.addItem(2048);
        box.select(2048);
        return box;
    }

}

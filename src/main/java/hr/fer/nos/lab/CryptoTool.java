package hr.fer.nos.lab;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Author: Marin Kukovačec
 */

@Theme("mytheme")
@Title("CryptoTool")
public class CryptoTool extends UI {

    private static final long serialVersionUID       = 1L;

    final Layout              entryLayout            = getEntryLayout();

    final Layout              cryptoLayout           = getCryptoLayout();

    final Layout              simetricCryptoLayout   = getSimetricCryptoLayout();

    final Layout              aSimetricCryptoLayout  = getAsimetricCryptoLayout();

    final Layout              digitalEnvelopeLayout  = getDigitalEnvelopeLayout();

    final Layout              digitalSignatureLayout = getDigitalSignatureLayout();

    final Layout              digitalStampLayout     = getDigitalStampLayout();

    static ComboBox           simetricMethods        = OptionComboboxes.getMethodsComboBox();
    static ComboBox           simetricCryptWay       = OptionComboboxes.getCryptWayComboBox();
    static ComboBox           simetricKeySize        = OptionComboboxes.getKeySizeComboBox();

    static ComboBox           asimetricMethods       = OptionComboboxes.getAMethodsComboBox();
    static ComboBox           asimetricHashFunctions = OptionComboboxes.getAHashComboBox();
    static ComboBox           asimetricKeySize       = OptionComboboxes.getAKeySizeComboBox();

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        changeLayout(entryLayout);

    }

    private Layout getEntryLayout() {
        final VerticalLayout layout = new VerticalLayout();
        final Button crypto = new Button("Digitalna omotnica");
        final Button digitalEnvelope = new Button("Digitalna omotnica");
        final Button digitalSignature = new Button("Digitalni potpis");
        final Button digitalStamp = new Button("Digitalna omotnica");

        crypto.addClickListener(click -> {
            changeLayout(cryptoLayout);
        });

        digitalSignature.addClickListener(click -> {
            changeLayout(digitalSignatureLayout);
        });

        digitalEnvelope.addClickListener(click -> {
            changeLayout(digitalEnvelopeLayout);
        });

        digitalStamp.addClickListener(click -> {
            changeLayout(digitalStampLayout);
        });

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponents(new Label("Digitalni pečat"), crypto, digitalSignature);

        return layout;
    }

    private Layout getCryptoLayout() {
        final VerticalLayout layout = new VerticalLayout();
        final Button sCrypto = new Button("Simetrično kriptiranje");
        final Button aCrypto = new Button("Asimetrično kriptiranje");
        final Button back = new Button("Povratak");

        sCrypto.addClickListener(click -> {
            changeLayout(simetricCryptoLayout);
        });

        aCrypto.addClickListener(click -> {
            changeLayout(aSimetricCryptoLayout);
        });

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponents(sCrypto, aCrypto, back);

        back.addClickListener(click -> {
            changeLayout(entryLayout);
        });

        return layout;
    }

    private Layout getSimetricCryptoLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Button back = new Button("Povratak");

        back.addClickListener(click -> {
            changeLayout(cryptoLayout);
        });

        final Layout cryptKey = getRow("Ključ kriptiranja");
        final Layout input = getRow("Ulazna datoteka");
        final Layout output = getRow("Izlazna datoteka");

        final Button crypt = new Button("Kriptiraj");

        final CheckBox decrypt = new CheckBox("Dekriptiranje");

        decrypt.addValueChangeListener(change -> {
            if ((Boolean) change.getProperty().getValue()) {
                crypt.setCaption("Dekriptiraj");
            }
            else {
                crypt.setCaption("Kriptiraj");
            }
        });

        crypt.addClickListener(click -> {
            try {
                final String newValue = SimetricCryptoUtils.execute(FileUtils.readFile(getComponentValue(input)),
                        FileUtils.readFile(getComponentValue(cryptKey)),
                        (String) simetricMethods.getValue(), (String) simetricCryptWay.getValue(), decrypt.getValue());

                FileUtils.writeToFile(getComponentValue(output), newValue.getBytes());
            }
            catch (final Exception ex) {
                Notification.show(ex.getMessage());
            }
        });

        final Button create = new Button("Generiraj");
        create.addClickListener(click -> {
            try {
                String path = getComponentValue(cryptKey);
                final File file = new File(path);
                if (file.isDirectory()) {
                    path += "/key.txt";
                }
                FileUtils.writeToFile(path,
                        SimetricCryptoUtils.generateKey((String) simetricMethods.getValue(), (int) simetricKeySize.getValue()));

            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        });
        cryptKey.addComponent(create);

        layout.addComponents(simetricMethods, simetricCryptWay, simetricKeySize, cryptKey, input, output, decrypt, crypt);

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(back);

        return layout;
    }

    private void changeLayout(final Layout layout) {
        setContent(layout);
    }

    private Layout getDigitalEnvelopeLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Button back = new Button("Povratak");

        back.addClickListener(click -> {
            changeLayout(entryLayout);
        });
        layout.addComponent(new Label("Digitalna omotnica"));
        final Layout input = getRow("Ulazna datoteka");
        final Layout publicKey = getRow("Javni ključ primatelja");
        final Layout output = getRow("Odredište generirane omotnice");
        final Button generateDigitalEnvelope = new Button("Generiraj digitalnu omotnicu");
        layout.addComponents(input, publicKey, output, generateDigitalEnvelope);

        generateDigitalEnvelope.addClickListener(click -> {
            try {
                final byte[] newValue = AsimetricCryptoUtils.execute(FileUtils.readFile(getComponentValue(input)),
                        AsimetricCryptoUtils.getPublicKey(getComponentValue(publicKey), "RSA"),
                        "RSA", false);

                FileUtils.writeToFile(getComponentValue(output), newValue);
            }
            catch (final Exception e) {

                e.printStackTrace();
            }
        });

        final Layout digitalEnvelope = getRow("Digitalna omotnica");
        final Layout privateKey = getRow("Tajni ključ primatelja");
        final Layout decryptOutput = getRow("Izlazna datoteka");
        final Button openDigitalEnvelope = new Button("Otvori digitalnu omotnicu");
        layout.addComponents(digitalEnvelope, privateKey, decryptOutput, openDigitalEnvelope);

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(back);

        return layout;
    }

    private Layout getDigitalSignatureLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Button back = new Button("Povratak");
        back.addClickListener(click -> {
            changeLayout(entryLayout);
        });

        layout.addComponent(new Label("Digitalni potpis"));
        final Layout input = getRow("Ulazna datoteka");
        final Layout cryptKey = getRow("Ključ kriptiranja");
        final Layout privateKey = getRow("Tajni ključ pošiljatelja");
        final Layout output = getRow("Odredište generiranog potpisa");
        final Button generateDigitalSignature = new Button("Generiraj digitalni potpis");
        layout.addComponents(asimetricHashFunctions, cryptKey, input, privateKey, output, generateDigitalSignature);

        generateDigitalSignature.addClickListener(click -> {
            try {
                final String path = getComponentValue(input);
                final String cryptPath = getComponentValue(cryptKey);

                final byte[] inputBytes = FileUtils.readFile(path);
                final byte[] inputCrypt = FileUtils.readFile(cryptPath);

                final byte[] concat = new byte[inputBytes.length + inputCrypt.length];
                System.arraycopy(inputBytes, 0, concat, 0, inputBytes.length);
                System.arraycopy(inputCrypt, 0, concat, inputBytes.length, inputCrypt.length);

                final String hash = DigestUtils.getDigestedMessage(concat, (String) asimetricHashFunctions.getValue());

                final Key key = AsimetricCryptoUtils.getPrivateKey(getComponentValue(privateKey), "RSA");

                final byte[] bytes = AsimetricCryptoUtils.execute(hash.getBytes(), key, "RSA", false);

                FileUtils.writeToFile(getComponentValue(output), bytes);
            }
            catch (final Exception ex) {
                ex.printStackTrace();
            }
        });

        final Layout digitalSignature = getRow("Digitalni potpis");
        final Layout publicKey = getRow("Javni ključ pošiljatelja");
        final Button openDigitalSignature = new Button("Provjeri digitalni potpis");
        layout.addComponents(digitalSignature, publicKey, openDigitalSignature);

        openDigitalSignature.addClickListener(click -> {
            try {
                final byte[] bytes = FileUtils.readFile(getComponentValue(digitalSignature));
                final Key key = AsimetricCryptoUtils.getPublicKey(getComponentValue(publicKey), "RSA");

                final byte[] inputBytes = FileUtils.readFile(getComponentValue(input));
                final byte[] inputCrypt = FileUtils.readFile(getComponentValue(cryptKey));

                final byte[] concat = new byte[inputBytes.length + inputCrypt.length];
                System.arraycopy(inputBytes, 0, concat, 0, inputBytes.length);
                System.arraycopy(inputCrypt, 0, concat, inputBytes.length, inputCrypt.length);

                final String hash = DigestUtils.getDigestedMessage(concat, (String) asimetricHashFunctions.getValue());

                final byte[] decrypted = AsimetricCryptoUtils.execute(bytes, key, "RSA", true);

                System.out.println(hash);
                System.out.print(new String(decrypted));

                if (hash.equals(new String(decrypted))) {
                    Notification.show("Ispravan potpis");
                }
                else {
                    Notification.show("Neispravan potpis");
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }

        });

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(back);

        return layout;
    }

    private Layout getDigitalStampLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Button back = new Button("Povratak");

        back.addClickListener(click -> {
            changeLayout(entryLayout);
        });

        layout.addComponent(new Label("Digitalni pečat"));
        layout.addComponent(getRow("Ulazna datoteka"));
        layout.addComponent(getRow("Javni ključ primatelja"));
        layout.addComponent(getRow("Tajni ključ pošiljatelja"));
        layout.addComponent(getRow("Digitalna omotnica"));
        layout.addComponent(getRow("Digitalni potpis"));
        layout.addComponent(getRow("Odredište generiranog pečata"));
        final Button generateDigitalEnvelope = new Button("Generiraj digitalnu omotnicu");
        layout.addComponent(generateDigitalEnvelope);

        layout.addComponent(getRow("Digitalni pečat"));
        layout.addComponent(getRow("Javni ključ pošiljatelja"));
        layout.addComponent(getRow("Tajni ključ primatelja"));
        layout.addComponent(getRow("Izlazna datoteka"));
        final Button openDigitalEnvelope = new Button("Otvori digitalni pečat");
        layout.addComponent(openDigitalEnvelope);

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(back);

        return layout;
    }

    private Layout getAsimetricCryptoLayout() {
        final VerticalLayout layout = new VerticalLayout();

        final Button back = new Button("Povratak");

        back.addClickListener(click -> {
            changeLayout(cryptoLayout);
        });

        final Layout cryptKey = getRow("Ključ kriptiranja");
        final Layout input = getRow("Ulazna datoteka");
        final Layout output = getRow("Izlazna datoteka");

        final Button crypt = new Button("Kriptiraj");

        final CheckBox decrypt = new CheckBox("Dekriptiranje");
        final String method = "RSA";
        decrypt.addValueChangeListener(change -> {
            if ((Boolean) change.getProperty().getValue()) {
                crypt.setCaption("Dekriptiraj");
            }
            else {
                crypt.setCaption("Kriptiraj");
            }
        });

        crypt.addClickListener(click -> {
            try {

                byte[] newValue = null;

                if (decrypt.getValue()) {
                    newValue = AsimetricCryptoUtils.execute(FileUtils.readFile(getComponentValue(input)),
                            AsimetricCryptoUtils.getPrivateKey(getComponentValue(cryptKey), method),
                            method, true);
                }
                else {
                    newValue = AsimetricCryptoUtils.execute(FileUtils.readFile(getComponentValue(input)),
                            AsimetricCryptoUtils.getPublicKey(getComponentValue(cryptKey), method),
                            method, false);
                }

                FileUtils.writeToFile(getComponentValue(output), newValue);
            }
            catch (final Exception ex) {
                Notification.show(ex.getMessage());
            }
        });

        final Button create = new Button("Generiraj");
        create.addClickListener(click -> {
            try {
                final String path = getComponentValue(cryptKey);
                final File file = new File(path);
                if (!file.exists() || file.isFile()) {
                    Notification.show("Neipravna datoteka");
                }
                else {
                    final String pubKey = path + "/publickey";
                    final String priKey = path + "/privatekey";
                    final KeyPair pair = AsimetricCryptoUtils.buildKeyPair(method, (int) asimetricKeySize.getValue());

                    FileUtils.writeToFile(pubKey, pair.getPublic().getEncoded());
                    FileUtils.writeToFile(priKey, pair.getPrivate().getEncoded());
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        });
        cryptKey.addComponent(create);

        layout.addComponents(asimetricMethods, asimetricKeySize, cryptKey, input, output, decrypt, crypt);

        layout.setMargin(true);
        layout.setSpacing(true);

        layout.addComponent(back);

        return layout;
    }

    private Layout getRow(final String title) {
        final BarLayout layout = new BarLayout();
        final Label labela = new Label(title);
        final TextField putanja = new TextField();
        putanja.setWidth("400px");
        putanja.setValue("/home/marin/Downloads/nos2lab");

        final Button peek = new Button("Pregledaj");
        layout.addComponents(labela, putanja, peek);

        peek.addClickListener(click -> {
            final File file = new File(getComponentValue(layout));
            try {
                if (!file.exists() || file.isDirectory()) {
                    Notification.show("Neipravna datoteka");
                }
                else {
                    Notification.show(new String(FileUtils.readFile(file.getAbsolutePath())));
                }
            }
            catch (final IOException ex) {
                Notification.show("Neipravna datoteka");
            }
        });

        return layout;
    }

    private String getComponentValue(final Layout layout) {
        final Iterator<Component> iterator = layout.iterator();
        iterator.next();
        final String value = ((TextField) ((FormLayout) iterator.next()).getComponent(0)).getValue();
        return value;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = CryptoTool.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {

        private static final long serialVersionUID = 1L;
    }
}

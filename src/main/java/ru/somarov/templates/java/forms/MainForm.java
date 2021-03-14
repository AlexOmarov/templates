package ru.somarov.templates.java.forms;


import javax.swing.*;
import javax.xml.soap.*;
import java.io.IOException;
import java.util.Objects;


public class MainForm extends JFrame{
    private JPanel rootPanel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton testButton;
    private JButton selectButton;
    private JButton countButton;
    private JPanel contentPanel;
    private JTextArea textArea1;

    private JComboBox metodicaField;
    private JComboBox tablenameField;
    private JTextField idRowField;
    private JTextField nameField;
    private JTextField textField5;
    private JTextField descriptionField;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField4;
    private JTextField textField9;
    private JComboBox periodField;
    private JComboBox firmaField;
    private SOAPConnectionFactory soapConnFactory;
    private SOAPConnection        soapConnection ;
    private MessageFactory        messageFactory ;

    private  static final String SOAP_ENDPOINT_URL = "http://ba40.ru/ba_xml.asmx?WSDL";
    private static final String MY_NAMESPACE = "http://tempuri.org/";
    private static final String MY_PASSWD = "ba40_masterkey_04";
    private static final String ID_BASE = "_p11_1";

    private void createConnection(){
        try {
            // Создание соединения
            soapConnFactory = SOAPConnectionFactory.newInstance();
            soapConnection  = soapConnFactory.createConnection();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadData(){
        SOAPMessage soapResponse;
        try {
            soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("GetDataTableFirma")), SOAP_ENDPOINT_URL);
            assert soapResponse != null;
            //TODO: Для каждой фирмы айтем в селекте формы

            soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("GetDataTablePeriods")), SOAP_ENDPOINT_URL);
            assert soapResponse != null;
            //TODO: Для каждого периода айтем в селекте формы

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private MainForm() {
        setContentPane(rootPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createConnection();
        loadData();


        countButton.addActionListener(e -> {
            SOAPMessage soapResponse;
            try {
                soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("RaschetMetodika")), SOAP_ENDPOINT_URL);
                assert soapResponse != null;
                textArea1.setText(soapResponse.getSOAPBody().getTextContent());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        selectButton.addActionListener(e -> {
                SOAPMessage soapResponse = null;
                try {
                    soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("GetDataTable")), SOAP_ENDPOINT_URL);
                    assert soapResponse != null;
                    textArea1.setText(soapResponse.getSOAPBody().getTextContent());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
        });
        addButton.addActionListener(e -> {
            SOAPMessage soapResponse;
            try {
                soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("Add").concat(Objects.requireNonNull(tablenameField.getSelectedItem()).toString())), SOAP_ENDPOINT_URL);
                assert soapResponse != null;
                textArea1.setText(soapResponse.getSOAPBody().getTextContent());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        deleteButton.addActionListener(e -> {
            SOAPMessage soapResponse;
            try {
                soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("Delete").concat(Objects.requireNonNull(tablenameField.getSelectedItem()).toString())), SOAP_ENDPOINT_URL);
                assert soapResponse != null;
                textArea1.setText(soapResponse.getSOAPBody().getTextContent());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        editButton.addActionListener(e -> {
            SOAPMessage soapResponse;
            try {
                soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("Edit").concat(Objects.requireNonNull(tablenameField.getSelectedItem()).toString())), SOAP_ENDPOINT_URL);
                assert soapResponse != null;
                textArea1.setText(soapResponse.getSOAPBody().getTextContent());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        testButton.addActionListener(e -> {
            SOAPMessage soapResponse;
            try {
                soapResponse = soapConnection.call(createSOAPRequest(MY_NAMESPACE.concat("HelloWorld")), SOAP_ENDPOINT_URL);
                assert soapResponse != null;
                textArea1.setText(soapResponse.getSOAPBody().getTextContent());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    private SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        String action = soapAction.replace(MY_NAMESPACE, "");

        if(!(action.equals("GetDataTable") && action.contains("GetDataTable"))){
            createStartingSoapEnvelope(soapMessage,soapAction,action);
        }
        else
        createSoapEnvelope(soapMessage,soapAction,action);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }


    private void createStartingSoapEnvelope(SOAPMessage soapMessage,String soapAction, String action) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("targetNamespace", MY_NAMESPACE);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("GetDataTable");
        SOAPElement soapBodyElem0 = soapBodyElem.addChildElement("numConnectFile");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("secretKey");
        soapBodyElem0.addTextNode(ID_BASE);
        soapBodyElem1.addTextNode(MY_PASSWD);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("tableName");
        String tablename = action.replace("GetDataTable", "");
        soapBodyElem2.addTextNode(tablename);

    }
    private void createSoapEnvelope(SOAPMessage soapMessage,String soapAction, String action) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("targetNamespace", MY_NAMESPACE);
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(action);
        SOAPElement soapBodyElem0 = soapBodyElem.addChildElement("numConnectFile");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("secretKey");
        soapBodyElem0.addTextNode(ID_BASE);
        soapBodyElem1.addTextNode(MY_PASSWD);
        if(action.equals("HelloWorld")){
            soapBody.addChildElement("HelloWorld");
        }
        else if(action.equals("GetDataTable")){
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("tableName");
            soapBodyElem2.addTextNode(Objects.requireNonNull(tablenameField.getSelectedItem()).toString());
        }
        else if(action.equals("RaschetMetodika")){
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("idMetodikaT");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("idFirma");
            SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("idPeriodT");
            soapBodyElem2.addTextNode(Objects.requireNonNull(metodicaField.getSelectedItem()).toString());
            soapBodyElem3.addTextNode("2");
            soapBodyElem4.addTextNode("1");

        }
        else if(action.contains("Add")){
            addingRow(soapAction.replace("Add", ""), soapBodyElem);
        }
        else if(action.contains("Delete")){
            deleteRow(soapAction.replace("Delete", ""), soapBodyElem);
        }
        else if(action.contains("Edit")){
            editRow(soapAction.replace("Edit", ""), soapBodyElem);
        }
    }

    private void addingRow(String tablename,SOAPElement soapBodyElem) throws SOAPException {
        if(tablename.equals("Firma")){
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("nameFirma");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("aboutFirma");
            soapBodyElem2.addTextNode(nameField.getText());
            soapBodyElem3.addTextNode(descriptionField.getText());

        }
    }

    private void deleteRow(String tablename,SOAPElement soapBodyElem) throws SOAPException {
        String id = idRowField.getText();
        if(tablename.equals("Firma")){
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("where_idFirma");
            soapBodyElem2.addTextNode(id);
        }
    }

    private void editRow(String tablename,SOAPElement soapBodyElem) throws SOAPException {
        if(tablename.equals("Firma")){
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("nameFirma");
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("aboutFirma");
            SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("where_idFirma");
            soapBodyElem2.addTextNode(nameField.getText());
            soapBodyElem3.addTextNode(descriptionField.getText());
            soapBodyElem4.addTextNode(idRowField.getText());
        }
    }


    public static void main(String[] args) {
        new MainForm();
    }

}

package kazpost.kz.mobterminal.ui.scanner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.BarcodeReaderInfo;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kazpost.kz.mobterminal.R;
import kazpost.kz.mobterminal.utils.AppConstants;

public class ScannerActivity extends AppCompatActivity implements
        BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.btn_switch)
    Button btnSwitch;
    private String mConnectedScanner = null;
    private BarcodeReader mBarcodeReader;

    private static BarcodeReader barcodeReader;
    private AidcManager manager;

    boolean useTrigger = true;
    boolean btnPressed = false;

    private AidcManager mAidcManager;

    public void initialize() {
        scannerSelection(mAidcManager.listConnectedBarcodeDevices());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        AidcManager.create(this, aidcManager -> {
            mAidcManager = aidcManager;
            mAidcManager.addBarcodeDeviceListener(event -> {
                // Could use this to call scannerSelection like when
                // press switch scanner button.
                // Here we just use it to notify the user when a scanner
                // is attached or detached and
                // give a toast.
                String connected;
                if (event.getConnectionStatus() == AidcManager.BARCODE_DEVICE_DISCONNECTED) {
                    connected = "Disconnected";
                } else {
                    connected = "Connected";
                }
                final String message = "Scanner: "
                        + event.getBarcodeReaderInfo().getFriendlyName() + " is "
                        + connected;
                this.runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
            });
            initialize();
        });


//        scannerSelection(mAidcManager.listConnectedBarcodeDevices());

        // create the AidcManager providing a Context and a
        // CreatedCallback implementation.
       /* AidcManager.create(this, aidcManager -> {
            manager = aidcManager;
            barcodeReader = manager.createBarcodeReader();

            try {
                if (barcodeReader != null) {
                    Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                    barcodeReader.claim();
                }
                // apply settings
                barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, false);
                barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                // set the trigger mode to automatic control
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
            } catch (UnsupportedPropertyException e) {
                Toast.makeText(ScannerActivity.this, "Failed to apply properties",
                        Toast.LENGTH_SHORT).show();
            } catch (ScannerUnavailableException e) {
                Toast.makeText(ScannerActivity.this, "Failed to claim scanner",
                        Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }

            // register bar code event listener
            barcodeReader.addBarcodeListener(ScannerActivity.this);
        });*/

    }


    private void scannerSelection(final List<BarcodeReaderInfo> scanners) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(() -> {
            final Dialog scannerSelectDialog = new Dialog(this);
            scannerSelectDialog.setContentView(R.layout.scanner_select_dialog);
            Button dialogButton = (Button) scannerSelectDialog
                    .findViewById(R.id.dialogButtonOK);

            // If there are scanners, just show the list, must select one
            if (scanners.size() > 0) {
                scannerSelectDialog.setTitle("Select Scanner");
                dialogButton.setVisibility(Button.INVISIBLE);
                final Map<String, String> scannerNames = new HashMap<>();
                for (BarcodeReaderInfo i : scanners) {
                    scannerNames.put(i.getFriendlyName(), i.getName());
                }

                final ListView list = (ListView) scannerSelectDialog
                        .findViewById(R.id.listScanners);
                ArrayAdapter<String> scannerNameAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1,
                        new ArrayList<>(scannerNames.keySet()));
                list.setAdapter(scannerNameAdapter);

                if (scannerNames.containsKey("8620903")){   //Internal Scanner, 8620903
//                    scannerNames.get("USB");
                    createBarcodeReaderConnection(scannerNames.get("8620903"));
//                    scannerSelectDialog.dismiss();
                }else{
                    showToastMsg("Подключите сканнер");
                }

//                list.setOnItemClickListener((myAdapter, myView, pos, mylng) -> {
//                    String selectedScanner = (String) list.getItemAtPosition(pos);
//                    createBarcodeReaderConnection(scannerNames.get(selectedScanner));
//                    scannerSelectDialog.dismiss();
//                });

            } else { // Show an ok button to close dialog
                scannerSelectDialog.setTitle("No Scanners Connected");
                dialogButton.setOnClickListener(v -> scannerSelectDialog.dismiss());
            }

//            scannerSelectDialog.setCancelable(false);
//            scannerSelectDialog.show();
        });
    }


    private void createBarcodeReaderConnection(String scanner) {

        if (scanner != null && !scanner.equals(mConnectedScanner)) {

            if (mBarcodeReader != null) {
                mBarcodeReader.release();
                mBarcodeReader.close();
            }

            try {
                mBarcodeReader = mAidcManager.createBarcodeReader(scanner);
                mBarcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);

            } catch (UnsupportedPropertyException e) {
                e.printStackTrace();
                Toast.makeText(this, "Control mode not set", Toast.LENGTH_SHORT).show();
            }

            mBarcodeReader.addBarcodeListener(this);
            mBarcodeReader.addTriggerListener(this);

            Map<String, Object> properties = new HashMap<String, Object>();
            // Set Symbologies On/Off
            properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
            properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
            // Set Max Code 39 barcode length
            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
            // Turn on center decoding
            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
            // Enable bad read response
            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
            // Apply the settings
            mBarcodeReader.setProperties(properties);

            claimBarcodeReader();
            mConnectedScanner = scanner;

            Intent intent = new Intent();
            intent.putExtra("scanner", scanner);
            setResult(1, intent);



            setScanner(scanner);
//            finish();
        }
    }

    private void setScanner(String scanner) {
        if (scanner != null) {

            // create the AidcManager providing a Context and a
            // CreatedCallback implementation.
            AidcManager.create(this, aidcManager -> {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader(scanner);

                try {
                    if (barcodeReader != null) {
                        Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                        barcodeReader.claim();
                    }
                    // apply settings
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, false);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                    // set the trigger mode to automatic control
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                            BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText(ScannerActivity.this, "Failed to apply properties",
                            Toast.LENGTH_SHORT).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText(ScannerActivity.this, "Failed to claim scanner",
                            Toast.LENGTH_SHORT).show();
                    //e.printStackTrace();
                }

                // register bar code event listener
                barcodeReader.addBarcodeListener(ScannerActivity.this);
            });

        }
    }


    public void claimBarcodeReader() {
        if (mBarcodeReader != null) {
            try {
                mBarcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick(R.id.button1)
    public void onButton1Click() {
        if (barcodeReader != null) {
            try {
                barcodeReader.softwareTrigger(true);
            } catch (ScannerNotClaimedException | ScannerUnavailableException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            showToastMsg("Barcodereader not available");
        }
    }


    @OnClick(R.id.btn_switch)
    public void onButtonSwitchClick() {
        startActivityForResult(new Intent(ScannerActivity.this, ScannerSelectionBarcodeActivity.class),
                AppConstants.ScannerSelectionBarcodeActivity);
    }

    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        // TODO Auto-generated method stub
        runOnUiThread(() -> {
            String barcodeData = barcodeReadEvent.getBarcodeData();
            String timestamp = barcodeReadEvent.getTimestamp();
            // update UI to reflect the data
            String s = (String) textView.getText();
            s += "\n" + barcodeData + "\n" + timestamp;
            textView.setText(s);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.ScannerSelectionBarcodeActivity) { //scannerSelectionBarcodeActivity
            if (data != null) {
                final String scanner = data.getStringExtra("scanner");


                // create the AidcManager providing a Context and a
                // CreatedCallback implementation.
                AidcManager.create(this, aidcManager -> {
                    manager = aidcManager;
                    barcodeReader = manager.createBarcodeReader(scanner);

                    try {
                        if (barcodeReader != null) {
                            Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                            barcodeReader.claim();
                        }
                        // apply settings
                        barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, false);
                        barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                        // set the trigger mode to automatic control
                        barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                                BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                    } catch (UnsupportedPropertyException e) {
                        Toast.makeText(ScannerActivity.this, "Failed to apply properties",
                                Toast.LENGTH_SHORT).show();
                    } catch (ScannerUnavailableException e) {
                        Toast.makeText(ScannerActivity.this, "Failed to claim scanner",
                                Toast.LENGTH_SHORT).show();
                        //e.printStackTrace();
                    }

                    // register bar code event listener
                    barcodeReader.addBarcodeListener(ScannerActivity.this);
                });

            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }

    @Override
    public void onResume() {  //will always? be called before app becomes visible?
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
                Log.d("noneywellscanner: ", "scanner claimed");
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (barcodeReader != null)
            barcodeReader.release();
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
    }
}

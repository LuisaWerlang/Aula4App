package com.example.aula4app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private final int SELECIONAR_CONTATO = 0;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onContatos(View view) {
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, SELECIONAR_CONTATO);
    }

    @Override
    protected void onActivityResult(int codigo, int resultado, Intent intent) {
        super.onActivityResult(codigo, resultado, intent);
        if (codigo == SELECIONAR_CONTATO) {

            if (resultado == RESULT_OK) {
                Uri uri = intent.getData();

                Cursor c = getContentResolver().query(uri, null, null, null, null);
                c.moveToNext();
                int nameCol = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
                int idCol = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                String nome = c.getString(nameCol);
                String id = c.getString(idCol);

                c.close();

                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                phones.moveToNext();
                String phoneNumber = phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phones.close();

                TextView txtNome = findViewById(R.id.txtNome);
                txtNome.setText(nome);

                TextView txtTelefone = findViewById(R.id.txtTelefone);
                txtTelefone.setText(phoneNumber);
            } else {
                Toast.makeText(this, "Nenhum contato selecionado", Toast.LENGTH_SHORT).show();
            }
        }

        if (codigo == REQUEST_IMAGE_CAPTURE && resultado == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView imageView = (ImageView) findViewById(R.id.foto);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void onWeb(View view) {
        Uri uri = Uri.parse("http://www.unisc.br");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    public void onCall(View view) {
        Uri uri = Uri.parse("tel: 995175175");
        Intent it = new Intent(Intent.ACTION_CALL, uri);
        startActivity(it);
    }

    public void onMaps1(View view) {
        Uri uriGeo = Uri.parse("geo:0,0?q=UNISC,Santa+Cruz+do+Sul");
        Intent it = new Intent(Intent.ACTION_VIEW, uriGeo);
        startActivity(it);
    }

    public void onMaps2(View view) {
        String localizacao = "geo:-25.443195,-49.280977";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(localizacao)));
    }

    public void onMaps3(View view) {
        String partida = "-25.443195, -49.280977";
        String destino = "-25.442207, -49.278403";
        String url = "http://maps.google.com/maps?f=d&saddr=" + partida + "&daddr=" + destino + "&hl=pt";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void onPicture(View view) {
        Intent takePicureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
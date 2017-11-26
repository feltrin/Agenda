package com.example.alura.agenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alura.agenda.dao.AlunoDAO;
import com.example.alura.agenda.modelo.Aluno;

import java.io.File;

public class FormularioActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 123;
    private FormularioHelper helper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        final Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        helper = new FormularioHelper (this);

        if(aluno != null){
            helper.preencheForm(aluno);
        }

        Button botaoCamera = findViewById(R.id.formulario_camera);
        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(intentCamera, CAMERA_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_CODE){
                helper.carregaFoto(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_form_ok:
                Aluno aluno = helper.pegaAluno();
                AlunoDAO dao = new AlunoDAO(this);

                if(aluno.getId() != null ){
                    dao.altera(aluno);
                }else {
                    dao.insere(aluno);
                }
                dao.close();

                Toast.makeText(FormularioActivity.this, "Aluno" + aluno.getNome() + "salvo!", Toast.LENGTH_SHORT).show();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

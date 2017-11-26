package com.example.alura.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alura.agenda.dao.AlunoDAO;
import com.example.alura.agenda.modelo.Aluno;

import java.util.List;

public class ListaAlunosActivity extends AppCompatActivity {

    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(i);

                Intent goToForm = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                goToForm.putExtra("aluno", aluno);

                startActivity(goToForm);
            }
        });

        Button novoAluno = (Button) findViewById(R.id.novo_aluno);
        novoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vaiProForm = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(vaiProForm);
            }
        });

        registerForContextMenu(listaAlunos);
    }

    private void carregaLista() {
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscaAlunos();
        dao.close();

        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        listaAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem site = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);

        String url = aluno.getSite();
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        intentSite.setData(Uri.parse(url));
        site.setIntent(intentSite);

        MenuItem sms = menu.add("SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        sms.setIntent(intentSMS);

        MenuItem mapa = menu.add("Mapa");
        Intent intentMap = new Intent(Intent.ACTION_VIEW);
        intentMap.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        mapa.setIntent(intentMap);

        MenuItem ligar = menu.add("Ligar");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,new String[]{Manifest.permission.CALL_PHONE}, 123);
                }else{
                    Intent intentCall = new Intent(Intent.ACTION_CALL);
                    intentCall.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentCall);
                }

                return false;
            }
        });

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deleta(aluno);
                dao.close();

                carregaLista();

                return false;
            }
        });
    }
}

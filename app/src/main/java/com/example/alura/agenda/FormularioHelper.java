package com.example.alura.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.alura.agenda.modelo.Aluno;

/**
 * Created by feltrin on 20/11/17.
 */

public class FormularioHelper {

    private EditText campoNome;
    private EditText campoEndereco;
    private EditText campoTelefone;
    private EditText campoSite;
    private RatingBar campoNota;
    private ImageView campoFoto;
    private Aluno aluno;

    public FormularioHelper(FormularioActivity activity) {
        campoNome = activity.findViewById(R.id.formulario_nome);
        campoEndereco = activity.findViewById(R.id.formulario_endereco);
        campoSite = activity.findViewById(R.id.formulario_site);
        campoTelefone = activity.findViewById(R.id.formulario_telefone);
        campoNota = activity.findViewById(R.id.formulario_nota);
        campoFoto = activity.findViewById(R.id.formulario_foto);
        aluno = new Aluno();
    }

    public Aluno pegaAluno(){
        aluno.setNome(campoNome.getText().toString());
        aluno.setEndereco(campoEndereco.getText().toString());
        aluno.setTelefone(campoTelefone.getText().toString());
        aluno.setSite(campoSite.getText().toString());
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());
        return aluno;
    }

    public void preencheForm(Aluno aluno) {
        this.aluno = aluno;
        campoNome.setText(aluno.getNome());
        campoEndereco.setText(aluno.getEndereco());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoNota.setProgress(aluno.getNota().intValue());
        carregaFoto(aluno.getCaminhoFoto());
    }

    public void carregaFoto(String caminhoFoto) {
        Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
        Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300,300,true);
        campoFoto.setImageBitmap(bitmapReduzido);
        campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}

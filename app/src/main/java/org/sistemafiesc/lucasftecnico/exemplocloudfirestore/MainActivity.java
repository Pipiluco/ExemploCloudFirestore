package org.sistemafiesc.lucasftecnico.exemplocloudfirestore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /**
     * Database = Collctions
     * Table = Document
     * Column = Field
     */
    FirebaseFirestore firestore;
    TextView tvDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        tvDisplay = (TextView) findViewById(R.id.tvDisplay);

        // adicionaContato();
        // lerContato();
        // lerObjetoContato();
        // atualizaContato();
        // excluiContato();
        atualizaRealTimeContato();
    }

    private void atualizaRealTimeContato() {
        DocumentReference contato = firestore.collection("ModuloRH").document("1");
        contato.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ERRO: ", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Toast.makeText(MainActivity.this, "Contato: " + documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void excluiContato() {
        firestore.collection("ModuloRH").document("1").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Contato excluído!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lerObjetoContato() {
        DocumentReference contato = firestore.collection("ModuloRH").document("1");
        contato.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Contato objetoContato = documentSnapshot.toObject(Contato.class);
                StringBuilder data = new StringBuilder("");
                data.append("Nome: ").append(objetoContato.getNome());
                data.append("\nE-mail: ").append(objetoContato.getEmail());
                data.append("\nTelefone: ").append(objetoContato.getTelefone());
                tvDisplay.setText(data.toString());
            }
        });
    }

    private void adicionaContato() {
        Map<String, Object> novoContato = new HashMap<>();
        novoContato.put("nome", "Lucas");
        novoContato.put("email", "lucas@gmail.com");
        novoContato.put("telefone", "99695-6573");

        firestore.collection("ModuloRH").document("1").set(novoContato).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Novo contato adicionado!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERRO: ", e.getMessage());
            }
        });
    }

    private void lerContato() {
        DocumentReference contato = firestore.collection("ModuloRH").document("1");
        contato.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documento = task.getResult();
                    StringBuilder data = new StringBuilder("");
                    data.append("Nome: ").append(documento.getString("nome"));
                    data.append("\nE-mail: ").append(documento.getString("email"));
                    data.append("\nTelefone: ").append(documento.getString("telefone"));
                    tvDisplay.setText(data.toString());
                }
            }
        });
    }

    private void atualizaContato() {
        DocumentReference contato = firestore.collection("ModuloRH").document("1");
        contato.update("nome", "Tibio").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Contato atualizado!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

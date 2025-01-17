package com.example.memorygame;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton[][] buttons;
    private int[][] imagens_jogo;
    private int[][] ids = {
            {R.id.b11, R.id.b12, R.id.b13, R.id.b14},
            {R.id.b21, R.id.b22, R.id.b23, R.id.b24},
            {R.id.b31, R.id.b32, R.id.b33, R.id.b34},
            {R.id.b41, R.id.b42, R.id.b43, R.id.b44}
    };
    private ImageButton primeiroBotao, segundoBotao;
    private boolean bloqueado = false; // Controla os cliques
    private int[] primeiraImagem = {-1, -1}; // Guarda a posição da primeira imagem
    private boolean jogoAtivo = false;
    private Button startRestartButton;
    private int paresRestantes = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        startRestartButton = findViewById(R.id.start);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttons = new ImageButton[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = findViewById(ids[i][j]);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnClickListener(view -> Click(finalI, finalJ));
            }
        }
    }

    public void Iniciar_Jogo(View view) {

        int[][] imagens = {
                {R.drawable.img1, R.drawable.img1, R.drawable.img2, R.drawable.img2},
                {R.drawable.img3, R.drawable.img3, R.drawable.img4, R.drawable.img4},
                {R.drawable.img5, R.drawable.img5, R.drawable.img6, R.drawable.img6},
                {R.drawable.img7, R.drawable.img7, R.drawable.img8, R.drawable.img8}
        };


        imagens_jogo = embaralharMatriz(imagens);
        jogoAtivo = true;
        paresRestantes = 8;


        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j].setImageResource(R.drawable.verso);
                buttons[i][j].setEnabled(true);
            }
        }
    }

    public void Click(int i, int j) {
        if (bloqueado || !jogoAtivo || buttons[i][j] == primeiroBotao) return;

        ImageButton botaoAtual = buttons[i][j];
        botaoAtual.setImageResource(imagens_jogo[i][j]);

        if (primeiraImagem[0] == -1) {
            primeiraImagem[0] = i;
            primeiraImagem[1] = j;
            primeiroBotao = botaoAtual;
        } else {
            segundoBotao = botaoAtual;
            bloquearBotoes();
            verificarPar(i, j);
        }
    }

    private void verificarPar(int i, int j) {
        if (imagens_jogo[i][j] == imagens_jogo[primeiraImagem[0]][primeiraImagem[1]]) {
            primeiroBotao.setEnabled(false);
            segundoBotao.setEnabled(false);
            paresRestantes--;  // Reduz o contador de pares restantes
            if (paresRestantes == 0) {
                jogoAtivo = false;  // Finaliza o jogo
                startRestartButton.setText("Start");  // Atualiza para "Start" ao fim do jogo
            }
            desbloquearBotoes();
        } else {
            new Handler().postDelayed(() -> {
                primeiroBotao.setImageResource(R.drawable.verso);
                segundoBotao.setImageResource(R.drawable.verso);
                desbloquearBotoes();
            }, 1000);
        }
        primeiraImagem[0] = -1;
    }

    private void bloquearBotoes() {
        bloqueado = true;
    }

    private void desbloquearBotoes() {
        bloqueado = false;
    }

    public int[][] embaralharMatriz(int[][] matriz) {
        List<Integer> list = new ArrayList<>();
        for (int[] row : matriz) {
            for (int id : row) {
                list.add(id);
            }
        }
        Collections.shuffle(list);

        int rows = matriz.length;
        int cols = matriz[0].length;
        int[][] shuffledMatriz = new int[rows][cols];
        int index = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                shuffledMatriz[i][j] = list.get(index++);
            }
        }

        return shuffledMatriz;
    }
}
import java.util.*;
public class Tabela {

    private int[][] tabela;
    private int numeroDeCidades;

    public int getNumeroDeCidades() {
        return numeroDeCidades;
    }

    public int[][] getTabela() {
        return tabela;
    }

    public Tabela(){
        Scanner ler = new Scanner(System.in);
        System.out.print("Informe o número de Cidades: ");
        this.numeroDeCidades = ler.nextInt();
        this.tabela = new int[numeroDeCidades][numeroDeCidades];
        if (leResposta()){
            for (int i = 0; i < numeroDeCidades; ++i){
                for (int j = 0; j < numeroDeCidades; ++j){
                    if (i == j){
                       tabela[i][j] = 0;
                    }
                    else if((j - i) == 1){
                        tabela[i][j] = 1;
                        tabela[j][i] = tabela[i][j];
                    }
                    else {
                        if (tabela[i][j] == 0){
                            tabela[i][j] = 10;
                            tabela[j][i] = tabela[i][j];
                        }
                    }

                }
            }
            tabela[0][numeroDeCidades -1] = 1;
            tabela[numeroDeCidades -1][0] = tabela[0][numeroDeCidades -1];
        }
        else {
            for (int i = 0; i < numeroDeCidades; ++i){
                for (int j = 0; j < numeroDeCidades; j++){
                    if (i != j){
                        if (tabela[i][j] == 0){
                            System.out.print("Informe a distância da cidade " + i +" para a cidade " + j + ": ");
                            tabela[i][j] = ler.nextInt();
                            tabela[j][i] = tabela[i][j];
                        }
                    }
                }
            }
        }
    }

    public void mostraTabela(){
        for (int i = 0; i < this.numeroDeCidades; ++i){
            System.out.println();
            for (int j = 0; j < this.numeroDeCidades; j++){
                System.out.print(this.tabela[i][j] + " ");
            }
        }
    }

    private boolean leResposta(){
        Scanner ler = new Scanner(System.in);
        String  resposta;
        System.out.println("Gerar grafo automático? (S ou N)");
        resposta = ler.nextLine();
        return resposta.equals("s") || resposta.equals("S");
    }


}



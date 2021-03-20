import java.util.*;
class Caminho {
    public ArrayList<Integer> caminho;
    private int score;

    public ArrayList<Integer> geraCaminho(int qnt, int[][] tab){ //tab = tabela das distancias entre cidades
                                                                // qnt = quantidade de cidades que compoem o caminho
        this.caminho = new ArrayList<Integer>(qnt + 1);
        this.caminho.add(0,0);
        ArrayList<Integer> possibilidades = new ArrayList<>();
        for (int i = 1; i < qnt; i++){
            possibilidades.add(i);
        }
        Collections.shuffle(possibilidades);
        for (int i = 0; i < qnt - 1; i++){
            this.caminho.add(possibilidades.get(i));
        }
        this.caminho.add(0);
        calculaScore(qnt,tab);
        return this.caminho;
    }

    public void calculaScore(int qnt, int[][] tab){
        int score = 0;
        int j = 1;
        for (int i = 0; j < qnt; j++){
            score += tab[this.caminho.get(i)][this.caminho.get(j)];
            i++;
        }
        score += tab[qnt - 1][0];
        this.score = score;
    }

    public void imprimeCaminho(){
       System.out.print("< ");
       for (int i = 0; i <this.caminho.size();i++ ){
           System.out.print(this.caminho.get(i)+", " );
       }
       System.out.println(" >");
       System.out.println("Score: " + this.score);
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCaminho(ArrayList<Integer> caminho) {
        this.caminho = caminho;
    }
}

public class Populacao{
     private Caminho [] populacao;
     private int tamPop = 1000;

     private void geraPop(int qnt, int[][] tab){
        this.populacao =  new Caminho [tamPop];
        for (int i = 0; i < tamPop;i++){
            Caminho novo = new Caminho();
            novo.geraCaminho(qnt,tab);
            this.populacao[i] = new Caminho();
            this.populacao[i] = novo;
        }
        ordenaPop(this.tamPop,this.populacao);
    }

     public void imprimePop(){
        for (int i = 0; i < tamPop; i++){
            this.populacao[i].imprimeCaminho();
        }
    }

     private void trocaPos(int pos1,int pos2, Caminho[] vetor){
         Caminho aux = vetor[pos1];
         vetor[pos1] = vetor[pos2];
         vetor[pos2] = aux;
    }

     private void ordenaPop(int tam, Caminho[] vetor){
        int [] scores = new int [tam];
        for (int i = 0; i < tam ;i++){
            scores[i] = vetor[i].getScore();
        }
        Arrays.sort(scores);

        for (int i = 0; i < tam; i++){
           if (vetor[i].getScore() != scores[i]){
               for (int j = 0; j < tam; j++){
                   if (vetor[j].getScore() == scores[i]){
                       trocaPos(i,j,vetor);
                   }
               }
           }
        }
    }

     private boolean EhValido(Caminho filho){
        for (int i = 1; i < filho.caminho.size();i ++){
            for (int j = i+1; j < filho.caminho.size(); j++){
                if (filho.caminho.get(i).equals(filho.caminho.get(j))){
                    return false;
                }
            }
        }
        return true;
    }

     private void validaFilho(int qnt,Caminho filho){
       ArrayList<Integer> naoRepetidos = new ArrayList<Integer>();
       ArrayList<Integer> repetidos = new ArrayList<Integer>();
       int cont = 0;

       for (int i = 1; i< qnt; i++){
           naoRepetidos.add(i);
       }

       for (int i = 1; i < filho.caminho.size();i ++){
           if (filho.caminho.contains(i)) {
               naoRepetidos.remove(Integer.valueOf(i));
           }
           for (int j = i+1; j < filho.caminho.size(); j++) {
                if (filho.caminho.get(i).equals(filho.caminho.get(j))) {
                    repetidos.add(filho.caminho.get(j));
                }
            }
       }

        for (int i = 1; i < filho.caminho.size();i ++){
            if (repetidos.size() > 0){
                if (filho.caminho.get(i).equals(repetidos.get(0))&& filho.caminho.get(i)!= 0){
                   if (cont == 0){
                       cont++;
                   }
                   else{
                       filho.caminho.set(i,naoRepetidos.get(0));
                       repetidos.remove(0);
                       naoRepetidos.remove(0);
                       cont =0;
                   }
                }
            }
        }

    }

     private Caminho[] geraFilhos(int qnt, int[][] tab){
         int numPais = 1000;
         Random rand1 = new Random();
         Random rand2 = new Random();
         Random chance = new Random();
         int tamCaminho = this.populacao[0].caminho.size();
         int porcentagem;
         Caminho[] filhos = new Caminho[numPais];
         Caminho filho;

        for (int i = 0; i < numPais; i++){
            porcentagem = chance.nextInt(100) + 1;
            if (porcentagem  <= 70){
                filho = Crossover(rand1.nextInt(250),rand2.nextInt(250));
            }
            else if (porcentagem > 70 && porcentagem < 90){
                filho = Crossover(rand1.nextInt(650) + 250,rand2.nextInt(650) + 250);
            }
            else {
                filho = Crossover(rand1.nextInt(100) + 900,rand2.nextInt(100) + 900);
            }
            while (!EhValido(filho)){
                validaFilho(qnt,filho);
            }

            filho.calculaScore(qnt,tab);
            filhos[i] = new Caminho();
            filhos[i].caminho = new ArrayList<Integer>(filho.caminho);
            filhos[i].setScore(filho.getScore());
            filho.caminho.clear();
            filho.setScore(0);
        }
        fazMutar(filhos,qnt,tab);
        ordenaPop(numPais,filhos);

        return filhos;
     }

     private Caminho Crossover(int pai1,int pai2){
        Caminho filho = new Caminho();
        filho.caminho = new ArrayList<Integer>();
        int tamCaminho = this.populacao[0].caminho.size();
        for (int j = 0; j < (tamCaminho/2); j++){
            int x = this.populacao[pai1].caminho.get(j);
            filho.caminho.add(x);
        }
        for (int k = 0; k < (tamCaminho/2); k++){
            int y = this.populacao[pai2].caminho.get((tamCaminho/2)+ k);
            filho.caminho.add(y);
        }
        return filho;
    }

     private void troca(int i,int j,Caminho filho){
        int aux1 = filho.caminho.get(i);
        int aux2 = filho.caminho.get(j);
        filho.caminho.set(i,aux2);
        filho.caminho.set(j,aux1);
    }

     private void fazMutar(Caminho[] filhos, int qnt, int[][] tab){
        Random rand = new Random();
        Random rand2 = new Random();
        int popMutada = 200;
        int tam = filhos[0].caminho.size() - 1;
        int ind;

        for (int i = 0; i < popMutada; i++){
            ind = rand2.nextInt(900) + 100;
            int posI = rand.nextInt(tam) + 1;
            int posJ = rand.nextInt(tam) + 1;

            if (posI > tam - 1){
                posI = tam - 1;
            }

            if (posJ > tam -1){
                posJ = tam - 1;
            }
            troca(posI,posJ,filhos[ind]);
            filhos[ind].calculaScore(qnt, tab);
        }

    }

     private void novaGeracao(int qnt, int[][] tab){
        Caminho[] filhos = geraFilhos(qnt,tab);
        int numDeFilhos = filhos.length;
        int j = this.tamPop - 1;
        for (int i = 0;i < numDeFilhos; i++){
                if (this.populacao[j].getScore() >= filhos[numDeFilhos - (1+ i)].getScore()){//Percorre os filhos do ultimo para o primeiro
                    this.populacao[j] = filhos[numDeFilhos - (1+ i)];
                    this.populacao[j].setScore(filhos[numDeFilhos - (1+ i)].getScore());
                    j--;
                }
        }

         ordenaPop(this.tamPop,this.populacao);
     }

     public static void PCV(){
         Tabela teste = new Tabela();
         Populacao pop = new Populacao();
         long tempoInicial = System.currentTimeMillis();
         for (int i=0; i<50; i++)
         {
             System.out.println();
         }
         pop.geraPop(teste.getNumeroDeCidades(),teste.getTabela());
         for (int i = 0; i <= 10000; i++){
             System.out.println("*  GERAÇÃO " + i + " *");
             pop.populacao[0].imprimeCaminho();
             System.out.println("");
             System.out.println("");
             System.out.println("* NOVA GERAÇÃO *");
             pop.novaGeracao(teste.getNumeroDeCidades(),teste.getTabela());
             pop.populacao[0].imprimeCaminho();
             System.out.println("");
             System.out.println("");
         }
         System.out.println("o tempo de execução em ms: " + (System.currentTimeMillis() - tempoInicial));
     }

}

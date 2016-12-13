import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Math;

/**
  * Graafi
  * <p>
  * Tietorakenteet 2015, harjoitustyö
  * <p>
  * Suunnattu graafi, joka toimii vierekkyyslistana.
  * @author Sami-Santeri Svensk (Svensk.Sami-Santeri.O@student.uta.fi)
  */

class TGraph
{
  //TGraph-luokkaan graafin tiedot välittävä taulukko.
  private int[][] graphdata=new int[99][5];

  //Arraylist, joka säilöö tiedostosta luetut solmut.
  private ArrayList<Vertex> solmutaulukko = new ArrayList<Vertex>();

  //Arraylist, joka tallentaa hakujen yhteydessä suoritetut vierailut.
  private ArrayList<Vertex> vierailut = new ArrayList<Vertex>();

  //Arraylist, joka tallentaa minimivirittävään puuhun tarjolla olevat kaaret.
  private ArrayList<Edge> prim = new ArrayList<Edge>();

  //Arratlist, joka tallentaa minimivirittävään puuhun tallennetut kaaret.
  private ArrayList<Edge> puu = new ArrayList<Edge>();
  
  public TGraph()
  {
     ReadData();
	 BuildGraph();
  }
  
  //Read in the content of data.txt.
  public void ReadData() 
  {
     String line;
	 try {
     BufferedReader br = new BufferedReader( new FileReader("data.txt"));
	 for(int i=0; i<99; i++)
	 {
	   line=br.readLine();
	   String[] values=line.split(",");
	   graphdata[i][0]=Integer.parseInt(values[0]); //x-coordinate of a vertex.
	   graphdata[i][1]=Integer.parseInt(values[1]); //y-coordinate of a vertex.
	   graphdata[i][2]=Integer.parseInt(values[2]); //first neighbor of a vertex.
	   graphdata[i][3]=Integer.parseInt(values[3]); //second neighbor of a vertex.
	   graphdata[i][4]=Integer.parseInt(values[4]); //third neighbor of a vertex.	   
	   
	 }
	 
	 } catch(IOException e)
	 {
	    System.out.println("File not found.");;
	 }
  }
  
 /**
   * Funktio, jossa luodaan solmu(Vertex)- ja kaariluokan(Edge) oliot.
   */
 public void BuildGraph()
  {

    //Solmujen luonti ja tallettaminen.
    System.out.println("Graafiin kuuluvat solmut:");
    for (int i = 0; i < graphdata.length; i++) {
        Vertex vertex = new Vertex(graphdata[i][0], graphdata[i][1], i);
        solmutaulukko.add(vertex);
        System.out.println(vertex.toString());
    }
    int indeksi = 0;

    //Kaarien luominen ja tallettaminen yleiseen listaan, ja jokaisen vertexin omaan kaarilistaan.
    for (int i = 0; i < graphdata.length; i++) {
        for (int j = 2; j < 5; j++) {
          Edge edge = new Edge(solmutaulukko.get(i), solmutaulukko.get(graphdata[i][j] - 1), indeksi);
          Vertex vertex = solmutaulukko.get(i);
          vertex.ulos(edge);
          Vertex solmu = solmutaulukko.get(graphdata[i][j] - 1);
          solmu.sisaan(edge);
          indeksi++;
        }
    }
  } 
  
  public int[][] GetGraphData()
  {
    return graphdata;
  }  

  /**
    * Leveyshakufunktio
    */
  @SuppressWarnings("unchecked")

  public void breadthSearch() {

      //Käytetään jonoa apuna indeksien järjestämiseen.
      Queue<Integer> q = new LinkedList<Integer>();

      //Alustetaan juurisolmuksi solmuja säilövän arrayn ensimmäinen alkio.
      Vertex v = solmutaulukko.get(0);

      //Lisätään alkion indeksi jonoon.
      q.add(v.indeksi());

      //Niin kauan kun jono on tyhjä...
      while(!q.isEmpty()) {
          for (int i = 0; i < solmutaulukko.get(q.peek()).ulos().size(); i++) {
              if (!solmutaulukko.get(q.peek()).ulos().get(i).to().vierailtu())
                 q.add(solmutaulukko.get(q.peek()).ulos().get(i).to().indeksi());
          }

          //Otetaan talteen ensimmäisenä jonoon lisätty indeksiarvo.
          int n = q.peek();

          //Poistetaan jonon ensimmäinen alkio.
          q.remove();

          //Jos solmussa ei olla vierailtu, lisätään indeksiarvon osoittama solmu vierailulistalle.
          if (!solmutaulukko.get(n).vierailtu()) {
             solmutaulukko.get(n).vieraile();
             vierailut.add(solmutaulukko.get(n));
          }
      }

      //Kutsutaan taulukon muodostavaa toTable-funktiota.
      bfstoTable();
      vierailut.clear();
      clearVisits();
  }

  /**
    * Funktio, joka muuttaa leveyshaussa kerätyn järjestetyn arrayn ohjelman käyttöliittymää
    * varten kaksiulotteiseksi int-taulukoksi.
    */

  public void bfstoTable() {

    //Kaksiulotteinen int-taulukko alustetaan vierailtujen solmujen lukumäärän kokoiseksi.
    graphdata = new int[vierailut.size()][5];
    System.out.println("Käydään solmut järjestyksessä");

    //Täytetään taulukkoa.
    for (int i = 0; i < vierailut.size(); i++) {

        //Sijoitetaan koordinaatit ensimmäisiin kahteen sarakkeeseen.
        graphdata[i][0] = vierailut.get(i).koordinaattiX();
        graphdata[i][1] = vierailut.get(i).koordinaattiY();

        //Tulostetaan solmun indeksi.
        System.out.println(vierailut.get(i).indeksi());

        //Uudesta taulukon järjestyksestä johtuen, täytyy naapurisolmujen taulukossa olevat indeksit asettaa uudestaan.
        for (int j = 0; j < vierailut.get(i).ulos().size(); j++) {
            for (int k = 0; k < vierailut.size(); k++) {
                if (vierailut.get(i).ulos().get(j).to().indeksi() == vierailut.get(k).indeksi()) {
                    graphdata[i][j+2] = k + 1;
                }
            }
        }
    }
  }

  /**
    * Syvyyshakufunktio, joka eroaa leveyshausta vain, että seuraavana vierailtavat solmut
    * tallennetaan jonon sijaan pinoon.
    */

  public void depthSearch() {

    //Käytetään pinoa säilömään vierailua odottavat solmut (niiden indeksit tässä tapauksessa)
    Deque<Integer> stack = new ArrayDeque<Integer>();

    //Asetetaan aloitussolmuksi listan ensimmäinen solmu.
    //Lisätään sen indeksi pinon ensimmäiseksi alkioksi.
    Vertex v = solmutaulukko.get(0);
    stack.push(v.indeksi());

    //Niin kauan kun pinossa on alkioita, jatketaan syvyyshakua.
    while(!stack.isEmpty()) {
        for (int i = 0; i < solmutaulukko.get(stack.peek()).ulos().size(); i++) {
            if (!solmutaulukko.get(stack.peek()).ulos().get(i).to().vierailtu())
               stack.push(solmutaulukko.get(stack.peek()).ulos().get(i).to().indeksi());
        }
        int n = stack.peek();
        stack.pop();

        if (!solmutaulukko.get(n).vierailtu()) {
            solmutaulukko.get(n).vieraile();
            vierailut.add(solmutaulukko.get(n));
        }
    }
    graphdata = new int[vierailut.size()][5];
    System.out.println("Käydään solmut järjestyksessä");
    for (int i = 0; i < vierailut.size() - 1; i++) {
      System.out.println(vierailut.get(i).indeksi());
        graphdata[i][0] = vierailut.get(i).koordinaattiX();
        graphdata[i][1] = vierailut.get(i).koordinaattiY();
        graphdata[i][2] = i+2;
        graphdata[i][3] = i+2;
        graphdata[i][4] = i+2;
    }

    vierailut.clear();
    clearVisits();
  }

  /**
    * Minimivirittävä puu Prim-Jarnikin algoritmillä.
    */

  public void minimumSpanningTree() {

    //Tehdään ensimmäinen sijoitus silmukan ulkopuolella.
    //Alustetaan aloitussolmu.
    Vertex v = solmutaulukko.get(0);
    v.lisaaPuuhun();
    for (int i = 0; i < v.kaikki().size(); i++) {
        prim.add(v.kaikki().get(i));
    }

    //Järjstetään array siihen sisältyvien kaarien pituuksien mukaan.
    prim = sort(prim);

    //Poistetaan ensimmäinen (lyhin) kaari, ja lisätään se puuhun.
    Edge e = prim.remove(0);
    puu.add(e);

    //Asetetaan seuraava tarkasteltava solmu.
    if (v.equals(e.to())) {
       v = solmutaulukko.get(e.from().indeksi());
    }

    else
       v = solmutaulukko.get(e.to().indeksi());
    
    //Merkitään, että solmu on kiinni puussa.
    solmutaulukko.get(v.indeksi()).lisaaPuuhun();

    //Silmukka
    while (!prim.isEmpty()) {

      //Lippumuuttuja, joka ilmaisee voiko tarkasteltavan kaaren lisätä vertailulistalle.
      boolean voidaanLisata = true;

      //Lisätään solmun kaaret listalle.
      //Tarkastetaan samalla, ettei puussa esiintyvää kaarta laiteta.
      //Tarkastetaan samala, ettei kaari viittaa solmuun itseensä.
      for (int i = 0; i < v.kaikki().size(); i++) {

          //Tarkastetaan ettei kaarta esiinny puussa.
          for (int j = 0; j < puu.size(); j++) {
             if (v.kaikki().get(i).equals(puu.get(j)) || v.kaikki().get(i).pituus() == 0) {
                voidaanLisata = false;
                break;
             }
             else
                voidaanLisata = true; 

          }
      
          //Jos kaarikandidaatti läpäisi edellisen testin, katsotaan ettei kaarta esiinny jo valmiiksi
          //vertailulistalla.
          if (voidaanLisata) {
             for (int j = 0; j < prim.size(); j++) {
                if (v.kaikki().get(i).equals(prim.get(j))) {
                   voidaanLisata = false;
                   break;
                }
                else
                   voidaanLisata = true;
             }
          }

          //Jos kaikki on kunnossa kaari voidaan lisätä.
          if (voidaanLisata) {
             prim.add(v.kaikki().get(i));
          }
      }

      //Järjestetään päivitetty array kaarien pituuksien mukaan.
      prim = sort(prim);

      //Lippumuuttuja, joka ilmoittaa jos lisättävä kaari on löydetty.
      boolean loydetty = false;

      while (!loydetty && !prim.isEmpty()) {

        //Otetaan arrayn lyhin kaari tarkasteluun.
        e = prim.remove(0);

        //Tarkastetaan, että ainakin toinen kaaren päistä ei ole puussa (syklitarkistus)
        //Jos ehto täytyy lisätään uusi kaari puuhun.
        if (!(solmutaulukko.get(e.to().indeksi()).puussa() && solmutaulukko.get(e.from().indeksi()).puussa())) {

          //Lisätään kaari puuhun.
          puu.add(e);

          //Lisätään kaaren molemmat päät puuhun.
          solmutaulukko.get(e.to().indeksi()).lisaaPuuhun();
          solmutaulukko.get(e.from().indeksi()).lisaaPuuhun();

          //Katsotaan seuraava solmu.
          if (v.equals(e.to())) {
            v = solmutaulukko.get(e.from().indeksi());
          }
          else {
            v = solmutaulukko.get(e.to().indeksi());
          }

          //Asetetaan lippumuuttuja todeksi.
          loydetty = true;
        }
      }

    }

    //Tulostetaan näytölle puuhun kuuluvat kaaret.
    System.out.println("Minimivirittävä puu koostuu seuraavista kaarista");

    for (int i = 0; i < puu.size(); i++) {
       System.out.println(puu.get(i));
    }
    System.out.println("Puun koko on " + puu.size());

    //Kutsutaan funktiota, jossa puu muutetaan taulukoksi.
    primToTable();

    //Tyhjennetään puu- ja prim-arrayt.
    puu.clear();
    prim.clear();
  }
    

  /**
    * Bubblesort-algoritmillä minimivirittävän puun kaaren sisältävän listan lajitteleva funktio.
    * @param Kaaria sisältävä arraylist.
    * @return järjestetty arraylist.
    */

  public ArrayList<Edge> sort(ArrayList<Edge> prim) {
    Edge[] taulukko = new Edge[prim.size()];

    for (int i = 0; i < prim.size(); i++) {
       taulukko[i] = prim.get(i);
    }
    prim.clear();

    boolean swap = false;
    int y = 1;
    Edge tmp;

    do {
      swap = false;
      for( int j = 0; j < taulukko.length - y; j++ ) {
        if( taulukko[j].pituus() > taulukko[j+1].pituus() ) {
          tmp = taulukko[j];
          taulukko[j] = taulukko[j+1];
          taulukko[j+1] = tmp;
          swap = true;
        }
      }
      y++;
    }
    while(swap);

    for (int i = 0; i < taulukko.length; i++) {
        prim.add(taulukko[i]);
    }

    return prim;
  }

  /**
    * Funktio, joka syöttää minimivirittävän puun kaaret kaksiulotteiseen int-taulukkoon.
    */

  public void primToTable() {
    //Alustetaan taulukko, johon tallennetaan minimivirittävän puun kaaret.
    int[][] jarjestys = new int[puu.size() + 1][2];

    //Täytetään taulukkoon kaaret. Ensimmäiseen sarakkeeseen kaaren toinen pää
    //ja toiseen sarakkeeseen toinen.
    for (int i = 0; i < jarjestys.length - 1; i++) {
        jarjestys[i][0] = puu.get(i).from().indeksi();
        jarjestys[i][1] = puu.get(i).to().indeksi();
    }

    //Lähdetään sijoittamaan kaaria oikeisiin lokeroihin. Ts. jokaisen taulukon indeksin kohdalla
    //on ensimmäisessä sarakkeessa kaaren se pää, joka lähtee kyseisen indeksin solmusta.
    //Tämä ei ole idioottivarma lajittelukeino, mutta pystyy lajittelemaan huomattavasti suurimman osan 
    //kaarista oikeille riveille.
    for (int i = 0; i < jarjestys.length; i++) {

       //Jos kumpikaan rivin sarakkeista ei ole yhtä suuri kuin laskuri, haetaan taulukosta seuraavilta
       //riveiltä ensimmäisenä käsiteltävä kaari, jossa jompi kumpi pää lähtee i-indeksin mukaisesta
       //solmusta.
       if (jarjestys[i][0] != i && jarjestys[i][1] != i) {
          for (int j = i + 1; j < jarjestys.length; j++) {

             //Jos löydetään sopiva kaari, tarkastellaan joudutaanko lukuja kääntämään rivillä.
             //Jos indeksin kanssa yhtä suuri luku löytyy ensimmäisestä sarakkeesta, tarvitsee
             //vain vaihtaa rivien arvoja päittäin.
             if (jarjestys[j][0] == i) {
                int tmp = jarjestys[j][0];
                int ttmp = jarjestys[j][1];
                jarjestys[j][0] = jarjestys[i][0];
                jarjestys[j][1] = jarjestys[i][1];
                jarjestys[i][0] = tmp;
                jarjestys[i][1] = ttmp;

                //Lopetetaan luuppi.
                break;
             }

             //Jos indeksi löytyy jälkimmäisestä sarakkeesta, tehdään sama operaatio kuin ylempänä,
             //mutta arvot sijoitetaan viimeisessä vaiheessa ristiin.
             else if (jarjestys[j][1] == i) {
                int tmp = jarjestys[j][0];
                int ttmp = jarjestys[j][1];
                jarjestys[j][0] = jarjestys[i][0];
                jarjestys[j][1] = jarjestys[i][1];
                jarjestys[i][0] = ttmp;
                jarjestys[i][1] = tmp;

                //Lopetetaan luuppi.
                break;
             }
          }
       }

       //Jos indeksi löytyy jälkimmäisestä sarakkeesta, vaihdetaan sarakkeiden arvojen paikkoja.
       else if (jarjestys[i][0] != i && jarjestys[i][1] == i) {
          int tmp = jarjestys[i][0];
          jarjestys[i][0] = jarjestys[i][1];
          jarjestys[i][1] = tmp;
       }
    }

    //Alustetaan ja sijoitetaan sopivat arvot graphdata-taulukkoon ohjelman käyttöliittymää varten.
    graphdata = new int[jarjestys.length][5];
    for (int i = 0; i < graphdata.length; i++) {
       graphdata[i][0] = solmutaulukko.get(jarjestys[i][0]).koordinaattiX();
       graphdata[i][1] = solmutaulukko.get(jarjestys[i][0]).koordinaattiY();
       graphdata[i][2] = jarjestys[i][1] + 1;
       graphdata[i][3] = jarjestys[i][1] + 1;
       graphdata[i][4] = jarjestys[i][1] + 1;
    }
  }

  /**
    * Lyhyimmän reitin kahden solmun välille etsivä algoritmi.
    */

  public void floyd() {

      //Luodaan kaksiulotteinen taulukko createMatrix-funktiolla.
      int[][] floyd = createMatrix();

      //Luodaan myös taulukko, johon listataan kahden solmun yhdistävä solmu.
      int[][] valipisteet = new int[solmutaulukko.size()][solmutaulukko.size()];

      //Floydin algoritmi.
      //Lähdetään vertaamaan taulukon pituuksia, jos löydetään kahden solmun välille lyhyempi reitti
      //käyttäen välisolmua, lisätään se alkuperäisen pituuden tilalle.
      for(int k = 0; k < floyd.length; k++){
         for(int i = 0; i < floyd.length; i++){
            for(int j = 0; j < floyd.length; j++){
               if (floyd[i][j] > floyd[i][k] + floyd[k][j]) {
                  floyd[i][j] = floyd[i][k] + floyd[k][j];
                  valipisteet[i][j] = k;
               }
            }
         }
      }

      int aloitus = 0;
      int maaranpaa = solmutaulukko.size() - 1;

      //Tulostetaan lyhin reitti.
      System.out.println("Lyhin etäisyys tiedoston ensimmäisestä solmusta tiedoston viimeiseen solmuun on " + floyd[aloitus][maaranpaa]);
      System.out.println();

      int n = maaranpaa;

      //Alustetaan array, johon tallennetaan polulla vastaan tulevat solmut.
      ArrayList<Integer> polku = new ArrayList<Integer>();

      //Kuljetaan taulukossa, kunnes määränpäästä päästään takaisin aloitukseen.
      while (n != aloitus) {
         polku.add(n);
         n = valipisteet[aloitus][n];
      } 
      polku.add(aloitus);

      System.out.println("Lyhin reitti kulkee seuraavien solmujen kautta:");

      //Tulostetaan polku näkyville.
      for (int i = polku.size() - 1; i >= 0; i--) {
         System.out.println(polku.get(i));
      }

      //Tehdään polusta esityskelpoinen.
      graphdata = new int[polku.size()][5];
      int j = 0;
      for (int i = polku.size() - 1; i >= 0; i--) {
         graphdata[j][0] = solmutaulukko.get(polku.get(i)).koordinaattiX();
         graphdata[j][1] = solmutaulukko.get(polku.get(i)).koordinaattiY();
         graphdata[j][2] = j + 2;
         graphdata[j][3] = j + 2;
         graphdata[j][4] = j + 2;
         j++;
      }
  }

  /**
    * Funktio, joka luo symmetrisen taulukon. solmujen yhdistäviä kaaria varten.
    * Tässä tapauksessa funktio luo taulukon lyhimmän reitin etsimistä varten, joten taulukkoon lisätään
    * kaarien pituudet.
    * @return symmetrinen int-taulukko, jossa solmujen etäisyydet naapureihinsa.
    */

  public int[][] createMatrix() {

    //Alustetaan taulukon kooksi solmujen lukumäärä.
    int[][] matrix = new int[solmutaulukko.size()][solmutaulukko.size()];

    //Alustetaan taulukko kolmessa osassa.

    //Ensin asetetaan mielivaltaisen suuri luku jokaiseen taulukon paikkaan.
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix.length; j++) {
            matrix[i][j] = 78978787;
        }
    }

    //Sitten täytetään täytettävissä olevat taulukon paikat kaarien pituuksilla. 
    //Esim. solmut indekseillä 0 ja 6 yhdistävän kaaren pituus sijoitetaan taulukon paikkaan
    //[0][6] ja symmetrisyyden takia myös paikkaan [6][0].
    for (int i = 0; i < matrix.length; i++) {

      for (int j = 0; j < matrix.length; j++) {

         //Katsotaan onko i:n ja j:n arvoilla olemassaolevaa kaarta.
         for (int k = 0; k < solmutaulukko.get(i).ulos().size(); k++) {

           //Jos i:n ja j:n arvoilla löytyy kaari, lisätään taulukkoon kaaren pituus.
           if (solmutaulukko.get(i).ulos().get(k).to().indeksi() == j || solmutaulukko.get(i).ulos().get(k).from().indeksi() == j) {
              matrix[i][j] = solmutaulukko.get(k).ulos().get(k).pituus();
           }
         }
      }
    }

    //Taulukon täytön 3. vaihe, jossa samat rivi- ja indeksiarvot saavat arvon 0. Solmustahan on siis 0 mittainen matka itseensä.
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix.length; j++) {
            if (i == j) {
               matrix[i][j] = 0;
            }
        }
    }

    //Palautetaan valmis taulukko.
    return matrix;
  }

  public void clearVisits() {
    for (int i = 0; i < solmutaulukko.size(); i++) {
       solmutaulukko.get(i).unohdaVierailu();
    }
  }
}
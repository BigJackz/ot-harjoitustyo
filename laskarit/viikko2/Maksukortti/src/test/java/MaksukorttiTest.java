import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaksukorttiTest {
    Maksukortti kortti;
    
    public MaksukorttiTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @After
    public void tearDown() {
    }

     //TODO add test methods here.
     //The methods must be annotated with annotation @Test. For example:
    
     @Test
     public void hello() {}
     
     @Test
     public void konstruktoriAsettaaSaldonOikein() {
         assertEquals("Kortilla on rahaa 10.0 euroa", kortti.toString());
     }
     @Test
     public void syoEdullisestiVahentaaSaldoaOikein() {
         kortti.syoEdullisesti();
         assertEquals("Kortilla on rahaa 7.5 euroa", kortti.toString());
     }
     @Test
     public void syoMaukkaastiVahentaaSaldoaOikein() {
         kortti.syoMaukkaasti();
         assertEquals("Kortilla on rahaa 6.0 euroa", kortti.toString());
     }
     @Test
     public void syoEdullisestiEiVieSaldoaNegatiiviseksi() {
         kortti.syoMaukkaasti();
         kortti.syoMaukkaasti();
         // saldo on 2
         kortti.syoEdullisesti();
         assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
     }
     @Test
     public void kortilleVoiLadataRahaa() {
         kortti.lataaRahaa(25);
         assertEquals("Kortilla on rahaa 35.0 euroa", kortti.toString());
     }
     @Test
     public void kortinSaldoEiylitaMakismiarvoa() {
         kortti.lataaRahaa(200);
         assertEquals("Kortilla on rahaa 150.0 euroa", kortti.toString());
     }
     @Test
     public void syoMaukkaastiEiVieSaldoaNegatiiviseksi() {
         kortti.syoMaukkaasti();
         kortti.syoMaukkaasti();
         // saldo 2 euro
         kortti.syoMaukkaasti();
         assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
     }
     @Test
     public void korttilleEiVoiLadataNegatiivistaRahaa() {
         kortti.lataaRahaa(-5);
         assertEquals("Kortilla on rahaa 10.0 euroa", kortti.toString());
     }
     @Test
     public void edullisenLounaanOstoKunRahaaTasanEdulliseenLounaaseen() {
         Maksukortti k = new Maksukortti(2.5);
         k.syoEdullisesti();
         assertEquals("Kortilla on rahaa 0.0 euroa", k.toString());
     }
     @Test
     public void maukkaanLounaanOstoKunRahaaTasanMaukkaaseenLounaaseen() {
         Maksukortti k = new Maksukortti(4.0);
         k.syoMaukkaasti();
         assertEquals("Kortilla on rahaa 0.0 euroa", k.toString());
     }
}
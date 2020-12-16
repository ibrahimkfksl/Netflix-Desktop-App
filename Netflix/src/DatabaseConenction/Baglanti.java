package DatabaseConenction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import netflix.FXMLDocumentController;

/**
*
* @author Abdullah Yasar Kisa
*/
public class Baglanti {

	private Connection con = null;// Database baglantisi icin
	private Statement st = null;// Database baglantisi icin
	private ResultSet rs = null;// sonuclari almak icin

	/*
	 * Constructor veritabanina baglanmak icin kullaniliyor
	 */
	public Baglanti() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String path = "ProLab3Database.accdb";
			String ur = "jdbc:ucanaccess://" + path;

			con = DriverManager.getConnection(ur);
			try {
				st = con.createStatement();

			} catch (Exception ex) {
				System.out.println("error occured " + ex);
				ex.printStackTrace();
				System.exit(1);
			}
		} catch (Exception x) {
			System.out.println("error occured" + x);
			System.exit(1);
		}

	}

	/*
	 * email ve parolayi alip veritabanindan kontrol ediyor Eger email sistemde
	 * kayitli ve sifre dogru ise kullanici idyi(kid) Eger email sistemde kayitli
	 * ama sifre yanlis sa -1 Eger kullanici sistemde kayitli degilse 0 donderiyor
	 */
	public Boolean girisYap(String email, String parola) {

		String sorgu = "Select * From Kullanici Where \"" + email + "\"=email;";
		

		try {
			rs = st.executeQuery(sorgu);
			while (rs.next()) {

				if (rs.getString("sifre").equals(parola)) {
					System.out.println("Kullanici email ve sifresi dogrudur giris yapiliyor");
					return true;
				} else if (!rs.getString("sifre").equals(parola)) {
					System.out.println("Kullanici Sifresi Hatali");
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("sql sorgusunda hata var");
			e.printStackTrace();
		}

		System.out.println("Girilen Mail Adresi Sistemde Kayitli Degil");
		return false;

	}

	/*
	 * eger metod true degerini geri dondurmus ise kullanici basarili bir sekilde
	 * kayit olmustur eger metod false degerini geri dondurmus ise kullanici kayit
	 * olamamistir. Sebebini ekrana cikti verir
	 */
	public Boolean kayitEt(String email, String isim, String sifre, String dogum_tarihi) {

		try {
			String email_kontrol = "Select email From Kullanici";
			rs = st.executeQuery(email_kontrol);
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					System.out.println("Bu Email ile Baska Bir Kullanici Kayit Olmus...");
					return false;
				}

			}

		} catch (SQLException e) {
			System.out.println("Yeni kullanici mail kontrolu sirasinda sql hatasi");
		}

		String yeni_kayit = "Insert Into Kullanici(email,isim,sifre,dogum_tarihi) VALUES(" + "'" + email + "','" + isim
				+ "','" + sifre + "','" + dogum_tarihi + "')";
		try {
			st.executeUpdate(yeni_kayit);
		} catch (SQLException e) {
			System.out.println("Yeni kullanici kayit sirasinda sql hatasi");
		}

		String kid_bul = "Select * From Kullanici";
		int kid = 0;
		try {
			rs = st.executeQuery(kid_bul);

			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					kid = rs.getInt("kid");
				}
			}

		} catch (SQLException e) {
			System.out.println("Yeni Kayit Olan Kullanicinin Kid Numarasini Araken SQL Hatasi Olustu");
			return false;
		}

		if (kid == 0) {
			System.out.println("Yeni Eklenen Kullanicinin Kid Numarasi Hatali Kontrol Ediniz");
			return false;
		}

		System.out.println("Kullanici Kaydi Basarili Bir Sekilde Yapilmistir..");
		return true;
	}

	/*
	 * eger metod true degerini geri dondurmus ise kullanici basarili bir sekilde
	 * kayit olmustur eger metod false degerini geri dondurmus ise kullanici kayit
	 * olamamistir. Sebebini ekrana cikti verir
	 */
	public Boolean kayitEmailKontrol(String email) {

		try {
			String email_kontrol = "Select email From Kullanici";
			rs = st.executeQuery(email_kontrol);
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					System.out.println("Bu Email ile Baska Bir Kullanici Kayit Olmus...");
					return false;
				}

			}

		} catch (SQLException e) {
			System.out.println("Yeni kullanici mail kontrolu sirasinda sql hatasi");
		}

		System.out.println("Bu mail ile kayitli kullanici yoktur");
		return true;
	}

	/*
	 * Yeni kullanicinin sevdigi turu kullaniciTur tablosuna ekliyor
	 */
	public void kullaniciSevilenTurKayit(String email, int tid) {

		int kid = 0;

		try {
			String tid_bul = "Select * From Kullanici";

			rs = st.executeQuery(tid_bul);
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					kid = rs.getInt("kid");
				}
			}

		} catch (SQLException e) {
			System.out.println("Yeni Kullanicinin Sevdigi Turlerin Tid Numaralarini Bulma Sirasinda SQL Hatasi");
		}

		try {
			String tid_ekle = "Insert Into KullaniciTur(kid,tid) VALUES(" + "'" + kid + "','" + tid + "')";
			st.executeUpdate(tid_ekle);

		} catch (SQLException e) {
			System.out.println("Yeni Kullanici icin KullaniciTur Tablosuna Satir Eklemede SQL Hatasi");
		}

		System.out.println("Yeni Kullanici Sevilen Tur Ekleme Islemi Tamamlanmistir");

	}

	/*
	 * Kullanicinin email ini alip kid numarasini geri donderiyor
	 */
	public int kullaniciIDBul(String email) {
		String sorgu = "Select * From Kullanici";

		try {
			rs = st.executeQuery(sorgu);
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					return rs.getInt("kid");

				}
			}

		} catch (SQLException e) {
			System.out.println("kullaniciSevilenTurKayit da sql sorgusu hatasi");
		}

		return 0;
	}

	/*
	 * Veritabaninda kayitli maksimum program sayisini buluyor
	 */
	public int maksProgramSayisi() {
		int maks_sayi = 0;

		String sorgu = "SELECT count(pid) FROM Program ";

		try {
			int kontrol = 0;
			rs = st.executeQuery(sorgu);
			while (rs.next()) {
				maks_sayi = rs.getInt(1);
				if (kontrol == 0) {
					break;
				}
			}

		} catch (SQLException e) {
			System.out.println("Maks Program Sayisi Bulunurken Sql Hatasi");
		}

		return maks_sayi;
	}

	/*
	 * Aldigi pid numarasindaki programin ismini geri donuyor pid numarasi min 1 den
	 * baslar
	 */
	public String programIsmıBul(int pid) {

		String sorgu = "Select programlar From Program Where pid=" + pid;

		try {
			rs = st.executeQuery(sorgu);
			rs.next();

			return rs.getString("programlar");

		} catch (SQLException e) {
			System.out.println("programIsimBul da sql sorgusu hatasi");
		}

		return null;
	}

	/*
	 * Aldigi pid numarasindaki programin bolum_sayisini geri donuyor pid numarasi
	 * min 1 den baslar
	 */
	public int programBolumSayisiBul(int pid) {

		String sorgu = "Select bolum_sayisi From Program Where pid=" + pid;

		try {
			rs = st.executeQuery(sorgu);
			rs.next();
			return rs.getInt("bolum_sayisi");

		} catch (SQLException e) {
			System.out.println("programBolumSayisiBul da sql sorgusu hatasi");
		}

		return 0;
	}

	/*
	 * Aldigi pid numarasindaki programin suresini geri donuyor pid numarasi min 1
	 * den baslar
	 */
	public int programSuresiBul(int pid) {

		String sorgu = "Select sure From Program Where pid=" + pid;

		try {
			rs = st.executeQuery(sorgu);
			rs.next();
			return rs.getInt("sure");

		} catch (SQLException e) {
			System.out.println("programSuresiBul da sql sorgusu hatasi");
		}

		return 0;
	}

	/*
	 * Aldigi pid numarasindaki programin puanini geri donuyor pid numarasi min 1
	 * den baslar
	 */
	public int programPuaniniBul(int pid) {

		String sorgu = "Select puan From Program Where pid=" + pid;

		try {
			rs = st.executeQuery(sorgu);
			rs.next();
			return rs.getInt("puan");

		} catch (SQLException e) {
			System.out.println("programPuaniniBul da sql sorgusu hatasi");
		}

		return 0;
	}

	/*
	 * Aldigi pid numarasindaki programin resminin veri yolunu string olarak geri
	 * donuyor pid numarasi min 1 den baslar
	 */
	public String programResimVeriYoluBul(int pid) {

		String sorgu = "Select resim_veri_yolu From Program Where pid=" + pid;

		try {
			rs = st.executeQuery(sorgu);
			rs.next();
			return rs.getString("resim_veri_yolu");

		} catch (SQLException e) {
			System.out.println("programPuaniniBul da sql sorgusu hatasi");
		}

		return null;
	}

	/*
	 * Aldigi pid numarasindaki programin turunu(1den fazla varsa sadece 1 ini)
	 * string olarak geri donuyor pid numarasi min 1 den baslar
	 */
	public String programTuruBul(int pid) {

		String sorgu = "Select * From ProgramTur Where pid=" + pid;
		int tid = 0;
		try {
			rs = st.executeQuery(sorgu);
			rs.next();

			tid = rs.getInt("tid");

		} catch (SQLException e) {
			System.out.println("programTuruBul-1 da sql sorgusu hatasi");
		}

		sorgu = "Select * From Tur Where tid=" + tid;
		try {
			rs = st.executeQuery(sorgu);
			rs.next();
			return rs.getString("tur_ismi");

		} catch (SQLException e) {
			System.out.println("programTuruBul-2 da sql sorgusu hatasi");
		}

		return null;
	}

	/*
	 * Kullanicinin sevdigi turlerin tid numaralarini dizi seklinde geri donderiyor
	 */
	public int[] kullaniciSevilenTurTidBul(int kid) {
		String sorgu = "Select * From KullaniciTur Where kid=" + kid;

		int turler[] = new int[3];
		int kontrol = 0;

		try {
			rs = st.executeQuery(sorgu);
			while (rs.next()) {
				turler[kontrol] = rs.getInt("tid");
				kontrol++;
			}

			return turler;

		} catch (SQLException e) {
			System.out.println("kullaniciSevilenTurTidBul da sql sorgusu hatasi");
		}

		return null;
	}

	/*
	 * Kullaniciya onerecegi programlarin pid lerinin bulundu 6 elemanli diziyi
	 * aliyor icini gonderilen tid numarasina sahip en yuksek puanli 2 filmi alip
	 * kendine gonderilen diziye kayit edip tekrar ayni diziyi donderiyor
	 */
	public int[] kullaniciOnerilenFilmBul(int[] kontrol, int tid) {
		String sorgu = "Select * From Program,ProgramTur Where ProgramTur.tid=" + tid
				+ " and Program.pid=ProgramTur.pid Order by Program.puan desc";
		int i;
		int maks_film = 0;
		try {
			rs = st.executeQuery(sorgu);

			while (rs.next()) {
				int pid = rs.getInt("pid");
				for (i = 0; i < 6; i++) {
					if (kontrol[i] == pid) {
						break;
					}
					if (kontrol[i] == 0) {
						kontrol[i] = pid;
						maks_film++;
						break;
					}

				}
				if ((maks_film == 2)) {
					break;
				}
			}

			return kontrol;
		} catch (SQLException e) {
			System.out.println("kullaniciOnerilenFilmBul metodunda sql hatasi");
		}

		return null;
	}

	/*
	 * kullanici programi baslattigi anda bu metot cagirilir. Eger kullanici daha
	 * once programi izlemis ise burada son izlenme tarihi guncelleninr Eger daha
	 * once hic izlenmemis se kullanici tabloya eklenip son izlenemsi yeniden
	 * eklenir
	 */
	public void kullaniciProgramIzle(int kid, int pid) {
		String sorgu = "Select * From KullaniciProgram";

		try {
			rs = st.executeQuery(sorgu);

			while (rs.next()) {

				if ((kid == rs.getInt("kid")) && (pid == rs.getInt("pid"))) {
					String zaman = LocalDateTime.now().toString();
					String sorgu2 = "Update KullaniciProgram Set son_izlenme='" + zaman + "' Where kid=" + kid
							+ " and pid=" + pid;

					st.executeUpdate(sorgu2);
					return;
				}

			}

			String zaman = LocalDateTime.now().toString();
			String sorgu3 = "Insert Into KullaniciProgram(kid, pid, son_izlenme,son_izlenen_bolum) VALUES(" + "'" + kid + "','" + pid
					+ "','" + zaman +"','"+ 1+ "')";

			st.executeUpdate(sorgu3);

		} catch (SQLException e) {
			System.out.println("kullaniciProgramIzle sql hatasi");

		}

	}

	/*
	 * Kullanicinin izledigi programin suresi tabloya eklenir
	 */
	public int kullaniciSureGuncelle(int kid, int pid) {
		String sorgu = "Select * From KullaniciProgram";
                int sure=0;
                kullaniciProgramIzle(kid, pid);
		try {  
			rs = st.executeQuery(sorgu);
                   
			while (rs.next()) {

				if ((kid == rs.getInt("kid")) && (pid == rs.getInt("pid"))) {
                                         sure = rs.getInt("son_kalinan_sure")
							+ (FXMLDocumentController.sure_bitis - FXMLDocumentController.sure_baslangic);
                                        sure/=60000;//sureyi dakika cinsinden tutuyoruz
                                        int bolum=rs.getInt("son_izlenen_bolum");
                                        String sorgu2="Select * From Program";
                                        
                                          
                                        ResultSet rs2=st.executeQuery(sorgu2);
                                        
                                        while(rs2.next()){
                                           
                                            
                                            if(("Dizi".equals(rs2.getString("tipi")))||("Tv Show".equals(rs2.getString("tipi")))){
                                                bolum+=(sure/40);
                                                sure=sure%40;
                                                
                                                String sorgu3 = "Update KullaniciProgram Set son_kalinan_sure=" + sure + " Where kid=" + kid
							+ " and pid=" + pid;
                                                
                                                 
					        st.executeUpdate(sorgu3);
                                                
                                                sorgu3 = "Update KullaniciProgram Set son_izlenen_bolum=" + bolum + " Where kid=" + kid
							+ " and pid=" + pid;
                                                  
					        st.executeUpdate(sorgu3);
                                                return sure;
                                            }
                                        }
                                        
                                        String sorgu3 = "Update KullaniciProgram Set son_kalinan_sure=" + sure + " Where kid=" + kid
							+ " and pid=" + pid;
                                          System.out.println("5");
                                        st.executeUpdate(sorgu3);
                                        
                                        sorgu3 = "Update KullaniciProgram Set son_izlenen_bolum=" + 1 + " Where kid=" + kid
                            			+ " and pid=" + pid;
                                          
				        st.executeUpdate(sorgu3);
                                        
					
				}
			}

		} catch (SQLException e) {
			System.out.println("kullaniciSureGuncelle sql hatasi");
		}
                return sure;
	}

	/*
	 * kullanici bastan izlemek isterse sure sifirlmasai yapiliyor
	 */
	public void kullaniciSureSifirla(int kid, int pid) {
		String sorgu = "Select * From KullaniciProgram";

		try {
			rs = st.executeQuery(sorgu);

			while (rs.next()) {

				if ((kid == rs.getInt("kid")) && (pid == rs.getInt("pid"))) {

					String sorgu2 = "Update KullaniciProgram Set son_kalinan_sure=" + 0 + " where kid=" + kid
							+ " and pid=" + pid;
					st.executeUpdate(sorgu2);
                                        
                                        
                                        sorgu2 = "Update KullaniciProgram Set son_izlenen_bolum=" + 1 + " where kid=" + kid
							+ " and pid=" + pid;
					st.executeUpdate(sorgu2);
				}
			}
		} catch (SQLException e) {
			System.out.println("kullaniciSureSifirla sql hatasi");
		}

	}

	/*
	 * Eger kullanici programa puan vermisse bu guncellenir
	 */
	public void kullaniciPuanGuncelle(int kid, int pid, int puan) {
		String sorgu = "Select * From KullaniciProgram";

		kullaniciProgramIzle(kid, pid);
		try {
			rs = st.executeQuery(sorgu);

			while (rs.next()) {

				if ((kid == rs.getInt("kid")) && (pid == rs.getInt("pid"))) {

					String sorgu2 = "Update Kullaniciprogram Set kullanici_puani=" + puan + " where kid=" + kid
							+ " and pid=" + pid;
					st.executeUpdate(sorgu2);
				}
			}
		} catch (SQLException e) {
			System.out.println("kullaniciPuanGuncelle sql hatasi");
		}

	}

	/*
	 * Kendine gonderilen diziye hangi turde program istesniyorsa pid numaralari
	 * eklenip geri gonderiliyor
	 */
	public int[] tureGoreFiltreleme(int[] programlar, String tur) {
		String tid_bul = "Select * From Tur";
		int tid = 0;
		int i = 0;
		try {
			rs = st.executeQuery(tid_bul);
			while (rs.next()) {
				if (tur.equals(rs.getString("tur_ismi"))) {
					tid = rs.getInt("tid");
				}
			}

			String programBul = "Select * From ProgramTur";

			rs = st.executeQuery(programBul);

			while (rs.next()) {
				if (tid == rs.getInt("tid")) {
					programlar[i] = rs.getInt("pid");
					i++;
				}
			}

		} catch (SQLException e) {
			System.out.println("tureGoreFiltreleme sql sorgu hatasi");
		}

		return programlar;
	}

        public int sonKalinanSureBul(int kid,int pid){
            String sorgu = "Select * From KullaniciProgram";

            
		try {
			rs = st.executeQuery(sorgu);

			while (rs.next()) {

				if ((kid == rs.getInt("kid")) && (pid == rs.getInt("pid"))) {

                                    System.out.println(rs.getInt("son_kalinan_sure"));
					return rs.getInt("son_kalinan_sure");
                                        
                                        
                                       
				}
			}
		} catch (SQLException e) {
			System.out.println("sonKalinanSureBul sql hatasi");
		}
            
            return 0;
        }

	

}

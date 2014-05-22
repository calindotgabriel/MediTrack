package ro.meditrack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import ro.meditrack.adapters.MedicamentAdapter;
import ro.meditrack.db.DatabaseHandler;
import ro.meditrack.model.Medicament;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by motan on 2/26/14.
 */
public class MedicamenteFragment extends Fragment {

    private ArrayList<Medicament> medicamente;
    private AutoCompleteTextView autoCompleteTextView;
    private Medicament medicamentAles;

    public void setAbTitle() {
        getActivity().getActionBar().setTitle("Medicamente");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAbTitle();
    }

    public void populateMedicamenteFromDb() {
        Resources resources = getResources();

        DatabaseHandler db = DatabaseHandler.getInstance(getActivity());

/*        for (int i = 0 ; i <= 6576 ; i++) {

            Medicament medicament = new Medicament(resources.getString(R.string.m_t_4),
                    resources.getString(R.string.m_p_i) );

            db.addMedicament(medicament);
        }*/


        if (db.isMedicamenteTableEmpty()) {
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_nurofen1), resources.getString(R.string.descriere_nurofen1)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_nurofen2), resources.getString(R.string.descriere_nurofen2)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_nurofen3), resources.getString(R.string.descriere_nurofen3)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_algocalmin), resources.getString(R.string.descriere_algocalmin)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_diazepam), resources.getString(R.string.descriere_diazepam)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_cipralex), resources.getString(R.string.descriere_cipralex)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_xanax), resources.getString(R.string.descriere_xanax)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_tantum1), resources.getString(R.string.descriere_tantum1)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_tantum2), resources.getString(R.string.descriere_tantum2)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_aspirina1), resources.getString(R.string.descriere_aspirina1)));
            db.addMedicament(new Medicament(resources.getString(R.string.medicament_n_0), resources.getString(R.string.medicament_p_0)));
        }
        List<Medicament> listaMedicamente = db.getAllMedicamente();

/*        for (Medicament m : listaMedicamente) {
            String log = m.getId() + ") [" + m.getName() + " & " + m.getDescirere() + "] \n";
            Log.d("MeowMedi", log);
        }*/

        medicamente = new ArrayList<Medicament>(listaMedicamente);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        populateMedicamenteFromDb();
        return inflater.inflate(R.layout.frame_medicament, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoCompleteTextView = (AutoCompleteTextView) getView()
                .findViewById(R.id.autocomplete_medicament);


        MedicamentAdapter adapter = new MedicamentAdapter(getActivity().getBaseContext(), R.layout.dropdown_medicament, medicamente);

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                medicamentAles = medicamente.get(position);
                autoCompleteTextView.setText(medicamentAles.getName());

                FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.medicament_container);
                frameLayout.setBackgroundColor(Color.TRANSPARENT);


                goToMedicamentField();

            }
        });
    }

    public void goToMedicamentField() {
        Fragment fragment = new MedicamentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nume_medicament", medicamentAles.getName());
        bundle.putString("descriere_medicament", medicamentAles.getDescirere());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.medicament_container, fragment).commit();
    }
}


/*    public void addMinedMedsToDB() {

        DatabaseHandler db = new DatabaseHandler(getActivity());*/

       /* db.addMedicament(new Medicament("ABAKTAL, comprimate", "Prospect <strong>Compozitie </strong>O tableta contine 400 de mg de pefloxacina sub forma de dihidrat de mesilat.  O fiola (5 ml) contine 400 de mg de pefloxacina, sub forma de mesilat.
                <strong>Actiune terapeutica </strong>Pefloxacina este un preparat antimicrobian semisintetic apartinand grupei chinolonelor.
                Are o actiune bactericida de inhibare a reproducerii bacteriene a adn, influentand, totodata, arn-ul
                si sinteza albuminelor bacteriene. Administrat oral, se resoarbe repede, atingand concentratia maxima dup
                a 90 de minute. Timpul de injumatatire biologica al pefloxacinei este de
                aproximativ 8 ore. Gratie volumului de distribuire mare, patrunde usor in tesuturi,
                organe si in fluidele corporale. De aceea, concentratia sa la nivelul valvei aortei si al celei mitrale,
                al muschiului inimii, al oaselor, al peritoneului si al lichidului peritoneal, al vezicii urinare, prostatei,
                al salivei si al sputei o depaseste pe cea din ser. Circa 60 % din pefloxacina este eliminata prin urina,
                aproximativ 30 % prin vezica biliara (in general nemodificata), partial si sub forma de substante derivate,
                cum ar fi dimetil pefloxacina, oxidul inactiv de pefloxacina si glucuronidul de pefloxacina.
                Pefloxacina are un spectru larg de actiune.
                Actioneaza asupra unui mare numar de microorganisme rezistente la alte antibiotice.
                Germenii sensibili la pefloxacina sunt: escherichia coli, Klebsiella spp., Enterobacter spp., Serratia spp.
                Proteus mirabilis, speciile indol-pozitive de Proteus, Citrobacter spp. Salmonella spp., Shigella spp
        Haemophilus spp., Staphylococcus spp. si neisseria gonorrhoeae. O sensibilitate medie a fost semnalata la Streptococcus spp., Pseudomonas spp., Acinetobacter spp., clostridium perfringens, Micoplasma spp. si Chlamydia spp. Rezistente s-au dovedit a fi microorganismele anaerobe gram negative de tipul Spirochaeta spp. si mycobacterium tuberculosis.
                <strong><strong>Indicatii</strong></strong> Infectii cauzate de bacterii sensibile la pefloxacina: ale aparatului urinar; ale cailor respiratorii; ale nasului, gatului si urechilor; ale aparatului genital; ale stomacului si cailor hepatobiliare; ale oaselor si articulatiilor; ale pielii; septicemii si endocardite; ale meningelor.  Abaktal monodoza are urmatoarele indicatii  -infectii urinare inferioare necomplicate la femei;  -infectii gonococice necomplicate;  -&quot;diareea calatorului&quot;.
        <strong>  Dozare si mod de administrare </strong> Doza medie de pefloxacina pentru adulti si copii peste 15 ani este de 800 de mg pe zi, indiferent daca medicamentul se administreaza oral sau parenteral. In cazul administrarii orale se vor lua cate 2 tablete pe zi: una dimineata, dupa micul dejun si a doua seara, dupa cina. Administrararea parenterala a Abaktalului se va face intravenos, sub forma de perfuzie sau intraperitoneal, intr-un interval de o ora, si anume de doua ori pe zi, dimineata si seara.  Continutul unei ampule (400 de mg) se va dilua in 250 de ml de solutie continand 5 % glucoza. Nu este permisa diluarea Abaktalului intr-o solutie de clorura de sodiu sau in orice alt tip de solutie care contine ioni de clor. In cazul infectiilor grave se poate incepe cu doze de 800 mg de Abaktal. Pacientilor prezentand boli ale ficatului li se vor administra, intr-un interval de o ora, 8 mg/kg corp sub forma de perfuzie, dupa cum urmeaza:  - o data pe zi (icter galben);  - la 36 de ore (ascita);  - la 48 de ore (icter galben si ascita).
        <strong>Contraindicatii </strong>  Pefloxacina este contraindicata in cazul pacientilor hipersensibili la chinolone, al copiilor sub 15 ani, al gravidelor si al mamelor care alapteaza, precum si in acela al celor care prezinta carente de glucoza-6-fosfat-dehidrogenaza.
                <strong>Masuri de precautie </strong>  Intrucat exista riscul producerii unei reactii fotosensibile, este de dorit ca in timpul terapiei cu Abaktal sa fie total evitata expunerea la ultraviolete.  In cazul bolnavilor care prezinta tulburari grave ale functiei hepatice trebuie adaptata doza.
                <strong>Reactii adverse </strong>  Pot aparea tulburari digestive, mialgii sau artralgii, hipersensibilitate la lumina, tulburari neurologice (dureri de cap, ameteala), trombocitopenie (la doze mai mari de 1 600 de mg pe zi)."));
        db.addMedicament(new Medicament("ABAKTAL, comprimate filmate", "Prospect
                <strong>Forma de prezentare:</strong> Comprimate filmate 400 mg; cutie x 1 blist. x 10 cpr.
                <strong><strong>Indicatii</strong>: </strong> La adult, in infectii severe cu bacili gram-negativ si cu stafilococi: septicemie si endocardita; meningita; infectii respiratorii si din sfera orl, renale, ginecologice, abdominale si hepato-biliare, osteo-articulare, cutanate; uretrita gonococica si nespecifica.
        <strong>Doze si administrare: </strong> 800 mg/zi oral in 2 prize, dimineata si seara, in timpul meselor. Uretritele gonococice - 800 mg in doza unica. Calea intravenos: perf. lent (peste o ora), dupa diluarea in 250 ml de glucoza 5% de 2 ori/zi, dimineata si seara. Nu se va dilua in solutie hidrosalina sau alta solutie ce contine ioni clorura. Nu se amesteca in perfuzie cu alte medicamente. Adm. intravenos 8 mg/kg: de 2 ori/zi in absenta ascitei sau a sindromului icteric; o data/zi in prezenta sindromului icteric; o data la 36 ore in caz de ascita; o data la 48 ore cand coexista ascita cu sindromul icteric; la varstnici 400 mg/zi, la 12 ore.
                <strong>Contraindicatii </strong> Alergie la chinolone; deficit de G-6-PDH; copii; sarcina si alaptare.
                <strong>Atentionari: </strong> Se va evita expunerea la radiatii solare sau UV. Se reduce efortul fizic, se prefera repausul la pat pe durata trat. miastenia gravis; IH severa; varstnici; predispozitie la tendinite induse de chinolone; pacientii sub tratament cronic cu corticosteroizi si cei cu predispozitie la convulsii.
        <strong>Reactii adverse:  </strong>reactii cutanate alergice si de fotosensibilizare la radiatii solare sau UV. dureri articulare si musculare, tendinite. trombocitopenie, rar neutropenie. Tulburari neurologice: cefalee, insomnie, convulsii si tulburari de conotienta. tulburari digestive: gastralgii, varsaturi. In caz de durere sau inflamarea tendonului lui achile se opreste administrarea si se recomanda repaus la pat."));
        db.addMedicament(new Medicament("ABELCET,fiole", "Prospect
                <strong>Actiune terapeutica</strong>  antibiotic, antifungic. A se vedea de asemenea amfotericina b dezoxicolat. Produsele bazate pe lipide maresc timpul de circulatie si biodistributia amfotericinei. Pentru ca produsele lipidice au tendinta sa stea in circulatie mult timp, ele se pot localiza si atinge concentratii crescute in tesuturile cu permeabilitate capilara crescuta ( inflamatii, infectii, si tumorile solide). Important este ca, cele trei produse ce contin amfotericina b bazata pe lipide (Abelcet, Ambisome, Amphotec) au proprietati fizice, chimice si biologice diferite si ca atare au proprietati farmacologice si efecte adverse diferite. Toate aceste produse au un timp de injumatatire terminal lung, ce variaza in functie de produs (6,3-8,6 h pentru AmBisome, 27,5-28,3 h pentru Amphotec, si aproximativ 173,4 h pentru Abelcet).
                <strong><strong>Indicatii</strong></strong>  Medicamentul este toxic. Este folosit in principal pentru pacientii cu infectii fungice progresive si potential fatale. Produsele pe baza de lipide scad semnificativ toxicitatea renala de aceea amfotericina b pe baza de lipide este recomandata pacientiilor cu disfunctii sau leziuni renale, cand amfotericina b nu se poate sau trebuie administrata. Abelcet: in infectiile fungice sistemice invazive care sunt refractare la amfotericina b dezoxicolat sau la care disfunctia sau toxicitatea renala face imposibila folosirea produsului dezoxicolat.
        <strong>Mod de administrare</strong>  intravenos- infectii fungice sistemice: 5 mg/kg/zi ca solutie de 1 mg/mL perfuzie si administrat la 2,5 mg/kg/zi.  la copii si la cei cu boli cardiovasculare, diluati medicamentul pana la concentratia finala de 2 mg/mL.  Daca perfuzarea dureaza mai mult de 2 ore, agitati continutul. Nu folositi filtru intern.
        <strong>Contraindicatii</strong>  A se vedea Amfotericina B dezoxicolat
                <strong>Precautii</strong>  Nu este folosit pentru a trata infectiile fungice noninvazive inclusiv candidoza orala, vaginala, esofagiana la pacientii cu un numar normal de neutrofile. A se folosi cu precautie in disfunctiile renale.  Prepararea pentru administrare a complexului Amfotericinei B pe baza de lipide (Abelcet): agitati usor fiola pana cand dispare sedimentul. Extrageti doza dorita din fiola folosind seringi sterile de 20 mL cu un ac de diametru 18. Demontati acul seringii si inlocuiti-l cu unul cu filtru de 5 microni. Fiecare ac cu filtru este folosit pentru o singura fiola. Inserati apoi acul cu filtru al seringii intr-o punga de perfuzie IV ce contine solutie dextroza 5% si goliti continutul seringii in punga. Concentratia perfuziei ar trebui sa fie 1 mg/mL. Pentru copii si la cei cu probleme CV medicamentul se poate dilua in solutie dextroza 5% pana la o concentratie recomandata de 2 mg/mL.  Observatii pentru pacient - familie:  Terapia cu amfotericina necesita, de obicei, un timp indelungat pentru a asigura un raspuns adecvat (eradicarea organismelor) si pentru a preveni recaderile. Anuntati medicului orice semn de anorexie, greata, varsaturi, cefalee, rash, febra sau frisoane. Anuntati orice schimbari ale aportului si eliminarilor si scaderea severa in greutate. Consumati lichide in cantitatile indicate de medic pentru a preveni efectele nefrotoxice. Anuntati simptomele neurologice cum ar fi tinitusul, tulburarea vederii sau vertijul. Anuntati medicului orice hemoragie, aparuta spontan, tumefactia tesuturilor moi ca si vertijul sau hipoacuzia.  Reduceti efectele gastro-intestinale au ajutorul antihistaminicelor sau antiemeticelor, administrate inainte de inceputul terapiei, sau prin administrarea medicamentului inainte de mese. Incercati consumarea unor mese frecvente si in cantitate redusa daca apare diaree.Reactia febrila se poate reduce odata cu terapia pe timp indelungat; durerile musculare pot fi datorate nivelelor scazute de potasiu.
                <strong>Reactii adverse</strong>  Reactiile sunt comune celor trei medicamente (Abelcet, AmBisome, Amphotec).  Cardiovasculare: hipotensiune, hipertensiune, stop cardiac, tahicardie.  Gastro-intestinale: greturi, varsaturi, diaree, hemoragiigastro-intestinale.  SNC: cefalee, anxietate, confuzie, leucoencefalopatie.  Respirator: insuficienta respiratorie, hipoxie, tuse persistenta, epistaxis, revarsat pleural, rinita.  Hematologice: trombocitopenie, anemie, leukopenie.  Dermatologice: iritatie, prurit, transpiratie.  Reactiila perfuzare: febra, tremor, frisoane, hipotensiune, anorexie, cefalee,tahipnee. Diverse: frisoane, febra, insuficienta multipla de organ,sepsis, reactie anafilactica, infectii, insuficienta renala, astenie,hematurie.  Exista si reactii speciale mentionate doar pentru Abelcet.  Cardiovasculare: stop cardiac, infarct miocardic, cardiomiopatii, aritmii inclusiv fibrilatii ventriculare.  Gastro-intestinale: melena, durere epigastrica.  SNC: convulsii, neuropatie periferica, AVC, encefalopatii, sindrom extrapiramidal. Hematologice: leucocitoza, eozinofilie.  Genito-urinare: oligurie, anurie, acidoza tubulara renala, impotenta.  Respiratorii: astm, bronhospasm, edem pulmonar, hemoptizie, embolie pulmonara, revarsat pleural.  Hepatice: hepatita, icter, insuficienta hepatica acuta, hepatomegalie, colangita, colecistita.  Dermatologice: Rash maculopapular, dermatita exfoliativa, eritem multiform. Musculoscheletale: miastenie, mialgie, artralgii.  Oftalmice: diplopie, tulburari vizuale.  Otice: pierderea auzului, hipoacuzie.  Diverse:scadere ponderala, reactii la locul injectarii (inclusiv inflamatie,reactii de tip anafilactic si alte reactii alergice), soc,tromboflebita, anorexie, acidoza.
                <strong>Interactiuni medicamentoase:</strong> A se vedea Amfotericina B dezoxicolat.
                <strong>Supradozarea:</strong> A se vedea Amfotericina B dezoxicolat"));
        db.addMedicament(new Medicament("Abilify", "Prospect
                <strong>Compozitie</strong> Abilify este un medicament care contine substanta activa aripiprazol. Este disponibil sub forma de comprimate (dreptunghiulare si albastre: 5 mg; dreptunghiulare si roz: 10 mg; rotunde si galbene: 15 mg; rotunde si roz: 30 mg), rotunde, comprimate orodispersabile (comprimate care se dizolva in gura); roz: 10 si 30 mg; galbene: 15 mg), solutie orala (1 mg/ml) si solutie injectabila (7,5 mg/ml).
                <strong>Actiune terapeutica</strong> Abilify se foloseste pentru tratarea adultilor care sufera de urmatoarele boli psihice: - schizofrenie, boala psihica care cuprinde mai multe simptome, printre care tulburari de gandire si vorbire, halucinatii (a auzi sau a vedea lucruri care nu sunt reale), suspiciune si iluzii (perceptii gresite); - afectiune bipolara tip I, o boala psihica in care pacientii au episoade maniacale (perioade de dispozitie anormal de buna) care alterneaza cu perioade de dispozitie normala. Pacientii pot avea si episoade de depresie. Abilify se utilizeaza pentru tratarea episoadelor maniacale moderate pana la severe si pentru prevenirea episoadelor maniacale la pacientii care au raspuns anterior la acest medicament. Solutia injectabila se foloseste pentru controlul rapid al starii de agitatie sau al comportamentului deviant, cand administrarea orala a medicamentului nu este adecvata.
                <strong><strong>Indicatii</strong></strong> Substanta activa din Abilify, aripiprazolul, este o substanta antipsihotica. Mecanismul exact de actiune nu este cunoscut, dar substanta se ataseaza de mai multi receptori diferiti de pe suprafata celulelor nervoase ale creierului. Acest lucru intrerupe semnalele transmise intre celule cerebrale prin intermediul „neurotransmitatorilor”, substante chimice care permit comunicarea intre celulele nervoase. Se crede ca aripiprazolul actioneaza in principal ca „agonist partial” al receptorilor pentru neurotransmitatorii dopamina si 5-hidroxitriptamina (numit si serotonina). Aceasta inseamna ca aripiprazolul actioneaza asemenea 5-hidroxitriptaminei si dopaminei activand acesti receptori, dar nu atat de puternic ca neurotransmitatorii. Deoarece dopamina si 5-hidroxitriptamina sunt implicate in schizofrenie si in afectiunea bipolara, aripiprazolul ajuta la normalizarea activitatii cerebrale, reducand simptomele psihotice sau maniacale si prevenind reaparitia lor.
        <strong>Doze si mod de administrare</strong> In schizofrenie, doza orala de inceput recomandata este de 10 mg sau 15 mg pe zi, urmata de o doza de intretinere de 15 mg o data pe zi. in afectiunea bipolara, doza orala de inceput recomandata este 15 mg pe zi, administrat singur sau in combinatie cu alte medicamente. La unii pacienti, poate fi nevoie de doze mai mari. Pentru prevenirea episoadelor maniacale, trebuie continuat cu aceeasi doza. in ambele boli, unii pacienti pot avea nevoie de doze mai mari. Solutia orala sau comprimatele orodispersabile pot fi folosite la pacientii care au dificultati de inghitire a comprimatelor. Comprimatele orodispersabile se iau punandu-se pe limba, de unde se dizolva rapid in saliva, sau se amesteca cu apa inainte de inghitire. Solutia injectabila se foloseste numai pe termen scurt si trebuie inlocuita cat mai repede posibil cu comprimate, comprimate orodispersabile sau solutie orala. Doza uzuala este de 9,75 mg ca injectie unica in muschiul umarului sau in muschiul fesier, dar doza eficace este cuprinsa intre 5,25 mg si 15 mg. O a doua injectie poate fi administrata la doua ore dupa prima, daca este necesar, dar intr-o perioada de 24 de ore nu trebuie administrate mai mult de trei injectii.
        <strong>Reactii adverse</strong> Cele mai frecvente efecte secundare asociate cu Abilify administrat oral (observate la 1 pana la 10 pacienti din 100) sunt: tulburari extrapiramidale (contractii si spasme necontrolate), acatisie (nevoia constanta de miscare), tremor (tremuraturi), somnolenta (starea de somn), sedare (toropeala), dureri de cap, incetosarea vederii, dispepsie (arsuri gastrice), varsaturi, greata (senzatia de rau), constipatie, hipersecretie salivara (productie marita de saliva), extenuare (slabiciune), stare de neliniste, insomnie (dificultati de a dormi) si anxietate. Acatisia este mai frecventa la pacientii cu afectiune bipolara decat la cei cu schizofrenie. intre 1 si 10 pacienti din 100 care au primit injectii cu Abilify au somnolenta, ameteli, dureri de cap, acatisie, greata si varsaturi."));
        db.addMedicament(new Medicament("ABIPLATIN, solutie injectabila 10, 25, 50 mg", "Prospect
                <strong>Proprietati farmacocinetice</strong>  In primele studii de farmacocinetica a cisplatinei pe animal si om,concentratia de platina totala s-a masurat prin spectrometrie cu absorbtie atomica sau prin administrarea medicamentului marcat radioactiv. Studii pe caini au aratat ca dupa administrarea intravenoasa a unei singure doze, nivelele plasmatice ale cisplatinei inregistreaza o curba bifazica cu un timp de injumatatire initial de 22 de minute si un timp de injumatatire final de aproape 5 zile. S-au determinat concentratii tisulare ridicate in rinichi, ficat, ovar siuter. Acelasi tip de curba bifazica se evidentiaza si la om dupa administrare in bolus. Timpul de injumatatire initial este de 25-49 min si timpul de injumatatire final de la 3 la 4 zile. Intr-un studiu recent raportand o faza rapida cu un timp de injumatatire de 23 min si o faza lenta cu un timp de injumatatire de 67 ore, valorile au fost inconcordanta cu observatiile efectuate anterior. Se poate spune ca apare si o a treia faza excretorie cu timp de injumatatire mai lung demonstrata de concentratia plasmatica de platina crescuta dupa 21 de zile. In timpul fazei terminale mai mult de 90% din medicament este legat de proteinele plasmatice.  Excretia urinara a produsului este incompleta: la 5 zile de la administrare se elimina in urina numai 27-45% din doza. Dozarile nivelelor de platina libera efectuate la om au evidentiat un timp mediu de injumatatire de 48 min dupa administrarea in bolus care corespunde probabil timpului deinjumatatire initial (25-49 min) observat atunci cand se monitorizeaza concentratia de platina totala si reflecta distributia medicamentului. Excretia urinara a platinei filtrabile este mai mare (75%) dupa perfuzie de 6 ore decat dupa injectarea aceleiasi doze de cisplatina in 15 min (40%). diureza indusa prin hidratare cu volum mare de lichid sau perfuzie cu manitol s-a asociat cu reducerea concentratiei platinum in urina. reducerea concentratiei de cisplatina determinata de un volum urinar mai mare poate avea un rol protector renal.  Activitatea pe tumori experimentale  Produsul s-a demonstrat a fi activ intr-un mare numar de tumori experimentale: - melanomul B16;  - carcinomul mamar CD8FI;  - tumoarea de colon 26;  - carcinosarcomul 256 Waiker;  - carcinomul pulmonar Lewis;  - leucemia P388;  - sarcomul osteogenic Ridgway;  Asociata cu alte produse, cisplatina a demonstrat o marcata actiune terapeutica sinergica impotriva tumorilor murine sistemice ( sarcom 180, leucemie L 1210, tumori mamare induse prin DMBA, reticulosarcom). Testele de rezistenta si rezistenta incrucisata pe diverse sublinii de L1210 si P388 sensibile si rezistente la cisplatina si la anumiti agenti alchilanti ( ciclofosfamida, melfalan, BCNU), au pus in evidenta diferite tipuri de raspuns indicand mecanisme de actiune diferite ale acestor agenti.
        <strong>Proprietati farmacodinamice </strong> Dupa cum au demonstrat studiile clinice efectuate in vitro, cisplatina actioneaza la nivelul ADN. Selectivitatea izomerului cis se poate datora capacitatii sale de a reactiona cu ADN-ul intr-o configuratie sterica particulara. Alterarea modelului ADN duce la inhibarea sintezei acestuia. Produsul nu actioneaza pe o faza anume a ciclului celular.
        <strong><strong>Indicatii</strong>  </strong> Cisplatina este indicata ca terapie paliativa in:  Tumori testiculare metastatice:  - cisplatina este utilizata in combinatie terapeutica cu alti agenti chimioterapici la pacienti cu tumori testiculare metastatice care au fost initial tratati cu radioterapie si/sau chirurgical;  - cisplatina poate fi utilizata in combinatie cu bleomycin sulfat si vinblastine sulfat;  Tumori ovariene metastatice:  - cisplatina este utilizata in combinatie terapeutica cu alti agenti chimioterapici la pacienti cu tumori ovariene metastalice care au beneficiat deja de tratamente radioterapice si/sau chirurgicale;  - cisplatina poate fi utilizata in combinatie cu adriamycin si/sau ciclofosfamida; Cisplatina ca agent unic este indicata ca tratament de linia a doua la pacienti cu tumori ovariene metastatice refractare la terapia standard, netratati initial cu cisplatina. Cancer avansat al vezicii: - cisplatina este indicata ca agent terapeutic unic pacientilor cu cancer avansat al vezicii, care nu mai raspund la radioterapie si/sau chirurgie; Cancer al capului si gatului:  - cisplatina in combinatii standard cu alte chimioterapice este indicata postradioterapie si/sau chirurgie la pacientii afectati de cancer al capului si gatului;
        <strong>Dozare si mod de administrare</strong>  Cisplatina trebuie administrata doar intravenos, in perfuzie lenta, asa cum este descris in continuare:  Acul intravenos si/sau setul de perfuzie nu trebuie sa contina nici o parte din aluminiu care poate intra in contact cu cisplatina. Aluminiul reactioneaza cu cisplatina provocand precipitare si scaderea activitatii.  Tumori testiculare metastatice:  O combinatie chimioterapeutica eficienta in tratarea acestor pacienti consta in cisplatina, bleomycin sulfat si vinblastin sulfat administrate dupa cum urmeaza:  - cisplatina 20 mg/metru patrat i.v. timp de 5 zile consecutive (ziua 1-5) repetate la fiecare 3 saptamani, timp de 3 cicluri.  - bleomycin sulfat: 30 u. i.v. saptamanal (ziua 2) timp de 12 doze consecutive;  - vinblastin sulfat: 0,15-0,20 mg/kg i.v. de 2 ori pe saptamana (zilele 1 si 2) la fiecare 3 saptamani, maximum 8 doze.  Tumori ovariene metastatice:  O combinatie chimioterapica eficienta in tratarea acestor bolnavi consta in cisplatina si adriamycin administrate dupa cum urmeaza:  - cisplatina: 50 mg/metru patrat i.v. la fiecare 3 saptamani (ziua 1);  - adriamycin: 50 mg/metru patrat i.v. la fiecare 3 saptamani (ziua 1);  Pentru administrarea de adriamycin se vor respecta instructiunile din ambalajul respectiv. In combinatie cisplatina cu adriamycin, cele doua medicamente trebuie administrate separat.  Ca agent terapeutic unic, cisplatina trebuie administrata in doza de 100 mg/metru patrat i.v., repetata la fiecare 4 saptamani. cancer avanat al vezicii urinare:  - cisplatina trebuie administrata ca agent unic chimioterapic in doza de 50-70 mg/metru patrat i.v. la fiecare 3 sau 4 saptamani, dupa indicatia medicului; - pacientii tratati in prealabil trebuie sa primeasca o doza initiala de 50 mg/metru patrat repetata la fiecare 4 saptamani;- perioada de tratament se stabileste in functie de prescriptia medicala; La administrarea cisplatinei trebuie luate in considerare urmatoarele principii:  - cisplatina trebuie administrata intr-o solutie pentru administrare intravenoasa continand cel putin 0,3% NaCl, aceasta cantitate de ioni clorura este esentiala pentru mentinerea cisplatinei stabila in solutie;  - cisplatina trebuie diluata in continuare in solutie 0,9% NaCI sau in 1/2 sau 1/3 N ser fiziologic continand 5% glucoza; - nefrotoxicitatea indusa de cisplatina poate fi redusa mentinand un debit urinar de cel putin 100 ml/h; aceasta se poate realiza prehidratand cu 2 I de solutie parenterala adecvata si posthidratand (cantitate recomandata: 2500 ml/metru patrat in 24 h); - daca hidratarea nu produce debitul urinar adecvat, poate fi administrat un diuretic osmotic (ca manitol);  - cisplatina in doza de 60 mg/metru patrat administrat in decurs de 1-2 ore este bine tolerat, doze mai mari trebuie administrate in decurs de 6-8 ore, cu suficienta hidratare pentru a mentine debitul urinar adecvat inainte si dupa perfuzie.  - administrarea cisplatinei poate duce la tulburari electrolitice si simptome de hipomagneziemie (electrolitii serici trebuie monitorizati inaintea, in timpul si dupa fiecare cura de cisplatina);  - nu trebuie administrata o noua doza de cisplatina atata timp cat creatinina serica este peste 1,5 mg/ml si/sau ureea sanguina (bun) este peste 25 mg/100 ml, iar elementele sanguine circulante nu sunt la un nivel acceptabil (trombocite peste 100 000/mm cubi, leucocite peste 4 000/mm cubi );  - dozele urmatoare nu trebuie administrate pana cand testele audiometrice nu arata acuitatea in limite normale;  Ca si alti agenti potentiali toxici, cisplatina trebuie manuita cu precautie.  Deoarece contactul cu pielea poate produce reactii locale, este recomandata utilizarea manusilor. Daca cisplatina vine in contact cu pielea sau mucoasele, acestea se vor spala imediat cu apa si sapun.
        <strong>Contraindicatii </strong> Antecedente de reactii alergice la cisplatina sau alte produse continand platina.
        <strong>Reactii adverse</strong>  Nefrotoxicitatea: insuficienta renala cu caracter cumulativ corelata cu doza este toxicitatea limitanta a dozei in cazul cisplatinei. Toxicitatea renala a fost observata la 28-36% dintre pacientii tratati cu doza unica de 50 mg/metru patrat.  Se manifesta in a doua saptamana dupa administrare, prin cresterea ureei sanguine, creatininei si acidului uric sanguin si/sau scaderea clearance-ului creatininei.  Au fost, de asemenea, raportate cazuri de microhematurie. Alterari ale functiei renale au fost asociate cu alterari tubulare renale. Pentru a reduce nefrotoxicitatea, cisplatina este administrata cu hidratare si manitol pentru a forta diureza pe o perioada de 6-8 ore, sau conform aprecierii medicului.Totusi, toxicitatea renala poate surveni in ciuda acestor masuri.  Ototoxicitatea: La mai mult de 31% dintre pacientii tratati cu doza unica de 50 mg/metri patrati cisplatina, ototoxicitatea se manifesta prin tinitus si/ sau pierderea auzului la frecventele inalte (4 000-8 000 Hz). Efectele ototoxice sunt mai severe la copii. pierderea auzului poate fi unilaterala sau bilaterala si tinde sa fie mai severa si mai frecventa cu repetarea dozelor. Nu este clar daca ototoxicitatea indusa decisplatina este reversibila.  Hematologice: Mielodepresia survine la unii pacienti tratati cu cisplatina. Nivelul minim al trombocitelor si leucocitelor survine intre zilele 18 si 23 (limite 7,5-45). Majoritatea pacientilor isi revin in jurul zilei 39 (limite 13-62). leucopenia si trombocitopenia sunt mai pronuntate la doze de peste 50 mg/metri patrati. anemia (scadere cu 2 g Hb/100 ml) survine cu frecventa similara.  Gastrointestinale: Greturi pronuntate si varsaturi survin la aproape toti pacientii tratati cu cisplatina, fiind uneori atat de severe incat impun oprirea tratamentului. Greturile si arsurile survin de obicei in timpul primelor 1-4 ore dupa tratament si dureaza pana la 24 de ore. Greturile si anorexia pot persista in grade variate pana la peste o saptamana dupa tratament.  Electrolitii serici: Hipomagneziemia, hipocalcemia si hipofosfatemia pot surveni la pacientii tratati cu cisplatina si sunt probabil datorate alterarilor tubulare renale. A fost ocazional raportata tetania la pacienti cu hipocalcemie si hipomagneziemie. Nivelele electrolitilor serici revin de obicei la normal dupa administrarea i.v.a suplimentelor de electroliti si intreruperea cisplatinei. hiperuricemia survine cu aceeasi frecventa ca si cresterea ureei sanguine si creatininei serice. Este mai pronuntata dupa doze mai mari de 50 mg/metru patrat. Valoarea maxima a acidului uric survine in general la 3-5 zile dupa administrarea cisplatinei. Terapia cu allopurinol poate reduce eficient nivelul acidului uric. Neurotoxicitatea: De obicei reprezentata de neuropatii periferice,survine la unii pacienti. Au fost descrise pierderea gustului si a perceptiei spatiale. Neuropatiile induse pot surveni dupa terapia prelungita (4-7 luni), dar au fost observate simptome si dupa o singuradoza. Datele preliminare sugereaza ca neuropatiile periferice pot fii reversibile la unii pacienti. A nu se utiliza produsul dupa data expirarii inscrisa pe ambalaj!
                <strong>interactiuni medicamentoase si alte interactiuni </strong> cisplatina este in general utilizata in combinatii chimioterapice. In acest caz, trebuie evaluat potentialul toxic sinergic, in special la nivel medular si renal.
        <strong>Atentionari si precautii speciale</strong>  Cisplatina induce nefrotoxicitate, mielodepresie si ototoxicitate, care pot agrava suferintele preexistente ale respectivelor organe.  Deoarece ototoxicitatea este cumulativa, trebuie efectuate teste audiometrice inaintea si in timpul tratamentului cu cisplatina. Examenele hematologice trebuie efectuate saptamanal.  Functia hepatica trebuie monitorizata periodic. De asemenea trebuie efectuate examene neurologice periodice.  Cisplatina trebuie administrata sub supravegherea unui medic specialist si doar in unitatile experimentate si dotate adecvat. Deoarece cisplatina produce nefrotoxicitate cumulativa, trebuie masurate inaintea inceperii tratamentului si inaintea fiecarei cure de cisplatina:  - creatinina serica;  - ureea; - clearance-ul creatininei; - nivelele de potasiu, magneziu si calciu;  Administrarea concomitenta de antibiotice aminoglicozide poate potenta nefrotoxicitatea cisplatinei. Nefrotoxicitatea creste cu repetarea ciclurilor de tratament. La doza recomandata, cisplatina nu trebuie administrata mai frecvent de o data la 3-4 saptamani. Trebuie asteptata normalizarea functiei renale inainte de readministrarea cisplatinei.  Ototoxicitatea este semnificativa, poate fi mai pronuntata la copii si se manifesta prin tinitus si/sau scaderea acuitatii la frecvente inalte.  Deoarece ototoxicitatea este cumulativa, trebuie efectuate teste audiometrice inaintea administrarii fiecarei doze de cisplatina. Reactiile anafilactice ca edem facial, bronhospasm, tahicardie si hipotensiune arteriala au fost, de asemenea, raportate.  Aceste reactii pot aparea la cateva minute dupa administrarea cisplatinei si sunt tratate cu adrenalina, corticosteroizi si antihistaminice.  Cisplatina este mutagenica la bacterii si produce aberatii cromozomiale in culturile tisulare. Cu toate ca teratogenitatea si carcinogenitatea nu au fost stabilite, compusii cu mecanism similar de actiune si activitate mutagena similara sunt considerati carcinogeni.
        <strong>Supradozare</strong>  Este vitala calcularea corecta a dozelor de cisplatina (de obicei in mg/metru patrat). In cazul aparitiei nefrotoxicitatii severe sau leucopeniei/trombocitopeniei, tratamentul trebuie intrerupt si luate masuri adecvate pentru corectarea acestora."));
    }*/

//}

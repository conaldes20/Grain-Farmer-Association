var wsUri = getRootUri() + "/FarmerAssociation/gfassociation";
var websocket;
var __connected = false;
var photo_base64str;
var accessToken = null;
var msg_to_client;
var new_group = false;
var group_list_update = false;

//var statelgovts = [
var statelgovts = {
    "Abia": [
      "Aba North",
      "Aba South",
      "Arochukwu",
      "Bende",
      "Ikwuano",
      "Isiala-Ngwa North",
      "Isiala-Ngwa South",
      "Isuikwato",
      "Obi Nwa",
      "Ohafia",
      "Osisioma",
      "Ngwa",
      "Ugwunagbo",
      "Ukwa East",
      "Ukwa West",
      "Umuahia North",
      "Umuahia South",
      "Umu-Neochi"
    ],
    "Adamawa": [
      "Demsa",
      "Fufore",
      "Ganaye",
      "Gireri",
      "Gombi",
      "Guyuk",
      "Hong",
      "Jada",
      "Lamurde",
      "Madagali",
      "Maiha",
      "Mayo-Belwa",
      "Michika",
      "Mubi North",
      "Mubi South",
      "Numan",
      "Shelleng",
      "Song",
      "Toungo",
      "Yola North",
      "Yola South"
    ],
    "Anambra": [
      "Aguata",
      "Anambra East",
      "Anambra West",
      "Anaocha",
      "Awka North",
      "Awka South",
      "Ayamelum",
      "Dunukofia",
      "Ekwusigo",
      "Idemili North",
      "Idemili south",
      "Ihiala",
      "Njikoka",
      "Nnewi North",
      "Nnewi South",
      "Ogbaru",
      "Onitsha North",
      "Onitsha South",
      "Orumba North",
      "Orumba South",
      "Oyi"
    ],
    "Akwa Ibom": [
      "Abak",
      "Eastern Obolo",
      "Eket",
      "Esit Eket",
      "Essien Udim",
      "Etim Ekpo",
      "Etinan",
      "Ibeno",
      "Ibesikpo Asutan",
      "Ibiono Ibom",
      "Ika",
      "Ikono",
      "Ikot Abasi",
      "Ikot Ekpene",
      "Ini",
      "Itu",
      "Mbo",
      "Mkpat Enin",
      "Nsit Atai",
      "Nsit Ibom",
      "Nsit Ubium",
      "Obot Akara",
      "Okobo",
      "Onna",
      "Oron",
      "Oruk Anam",
      "Udung Uko",
      "Ukanafun",
      "Uruan",
      "Urue-Offong/Oruko ",
      "Uyo"
    ],
    "Bauchi": [
      "Alkaleri",
      "Bauchi",
      "Bogoro",
      "Damban",
      "Darazo",
      "Dass",
      "Ganjuwa",
      "Giade",
      "Itas/Gadau",
      "Jama'are",
      "Katagum",
      "Kirfi",
      "Misau",
      "Ningi",
      "Shira",
      "Tafawa-Balewa",
      "Toro",
      "Warji",
      "Zaki"
    ],
    "Bayelsa": [
      "Brass",
      "Ekeremor",
      "Kolokuma/Opokuma",
      "Nembe",
      "Ogbia",
      "Sagbama",
      "Southern Jaw",
      "Yenegoa"
    ],
    "Benue": [
      "Ado",
      "Agatu",
      "Apa",
      "Buruku",
      "Gboko",
      "Guma",
      "Gwer East",
      "Gwer West",
      "Katsina-Ala",
      "Konshisha",
      "Kwande",
      "Logo",
      "Makurdi",
      "Obi",
      "Ogbadibo",
      "Oju",
      "Okpokwu",
      "Ohimini",
      "Oturkpo",
      "Tarka",
      "Ukum",
      "Ushongo",
      "Vandeikya"
    ],
    "Borno": [
      "Abadam",
      "Askira/Uba",
      "Bama",
      "Bayo",
      "Biu",
      "Chibok",
      "Damboa",
      "Dikwa",
      "Gubio",
      "Guzamala",
      "Gwoza",
      "Hawul",
      "Jere",
      "Kaga",
      "Kala/Balge",
      "Konduga",
      "Kukawa",
      "Kwaya Kusar",
      "Mafa",
      "Magumeri",
      "Maiduguri",
      "Marte",
      "Mobbar",
      "Monguno",
      "Ngala",
      "Nganzai",
      "Shani"
    ],
    "Cross River": [
      "Akpabuyo",
      "Odukpani",
      "Akamkpa",
      "Biase",
      "Abi",
      "Ikom",
      "Yarkur",
      "Odubra",
      "Boki",
      "Ogoja",
      "Yala",
      "Obanliku",
      "Obudu",
      "Calabar South",
      "Etung",
      "Bekwara",
      "Bakassi",
      "Calabar Municipality"
    ],
    "Delta": [
      "Oshimili",
      "Aniocha",
      "Aniocha South",
      "Ika South",
      "Ika North-East",
      "Ndokwa West",
      "Ndokwa East",
      "Isoko south",
      "Isoko North",
      "Bomadi",
      "Burutu",
      "Ughelli South",
      "Ughelli North",
      "Ethiope West",
      "Ethiope East",
      "Sapele",
      "Okpe",
      "Warri North",
      "Warri South",
      "Uvwie",
      "Udu",
      "Warri Central",
      "Ukwani",
      "Oshimili North",
      "Patani"
    ],
    "Ebonyi": [
      "Afikpo South",
      "Afikpo North",
      "Onicha",
      "Ohaozara",
      "Abakaliki",
      "Ishielu",
      "lkwo",
      "Ezza",
      "Ezza South",
      "Ohaukwu",
      "Ebonyi",
      "Ivo"
    ],
    "Enugu": [
      "Enugu South,",
      "Igbo-Eze South",
      "Enugu North",
      "Nkanu",
      "Udi Agwu",
      "Oji-River",
      "Ezeagu",
      "IgboEze North",
      "Isi-Uzo",
      "Nsukka",
      "Igbo-Ekiti",
      "Uzo-Uwani",
      "Enugu Eas",
      "Aninri",
      "Nkanu East",
      "Udenu."
    ],
    "Edo": [
      "Esan North-East",
      "Esan Central",
      "Esan West",
      "Egor",
      "Ukpoba",
      "Central",
      "Etsako Central",
      "Igueben",
      "Oredo",
      "Ovia SouthWest",
      "Ovia South-East",
      "Orhionwon",
      "Uhunmwonde",
      "Etsako East",
      "Esan South-East"
    ],
    "Ekiti": [
      "Ado",
      "Ekiti-East",
      "Ekiti-West",
      "Emure/Ise/Orun",
      "Ekiti South-West",
      "Ikare",
      "Irepodun",
      "Ijero,",
      "Ido/Osi",
      "Oye",
      "Ikole",
      "Moba",
      "Gbonyin",
      "Efon",
      "Ise/Orun",
      "Ilejemeje."
    ],
    "FCT - Abuja": [
      "Abaji",
      "Abuja Municipal",
      "Bwari",
      "Gwagwalada",
      "Kuje",
      "Kwali"
    ],
    "Gombe": [
      "Akko",
      "Balanga",
      "Billiri",
      "Dukku",
      "Kaltungo",
      "Kwami",
      "Shomgom",
      "Funakaye",
      "Gombe",
      "Nafada/Bajoga",
      "Yamaltu/Delta."
    ],
    "Imo": [
      "Aboh-Mbaise",
      "Ahiazu-Mbaise",
      "Ehime-Mbano",
      "Ezinihitte",
      "Ideato North",
      "Ideato South",
      "Ihitte/Uboma",
      "Ikeduru",
      "Isiala Mbano",
      "Isu",
      "Mbaitoli",
      "Mbaitoli",
      "Ngor-Okpala",
      "Njaba",
      "Nwangele",
      "Nkwerre",
      "Obowo",
      "Oguta",
      "Ohaji/Egbema",
      "Okigwe",
      "Orlu",
      "Orsu",
      "Oru East",
      "Oru West",
      "Owerri-Municipal",
      "Owerri North",
      "Owerri West"
    ],
    "Jigawa": [
      "Auyo",
      "Babura",
      "Birni Kudu",
      "Biriniwa",
      "Buji",
      "Dutse",
      "Gagarawa",
      "Garki",
      "Gumel",
      "Guri",
      "Gwaram",
      "Gwiwa",
      "Hadejia",
      "Jahun",
      "Kafin Hausa",
      "Kaugama Kazaure",
      "Kiri Kasamma",
      "Kiyawa",
      "Maigatari",
      "Malam Madori",
      "Miga",
      "Ringim",
      "Roni",
      "Sule-Tankarkar",
      "Taura",
      "Yankwashi"
    ],
    "Kaduna": [
      "Birni-Gwari",
      "Chikun",
      "Giwa",
      "Igabi",
      "Ikara",
      "jaba",
      "Jema'a",
      "Kachia",
      "Kaduna North",
      "Kaduna South",
      "Kagarko",
      "Kajuru",
      "Kaura",
      "Kauru",
      "Kubau",
      "Kudan",
      "Lere",
      "Makarfi",
      "Sabon-Gari",
      "Sanga",
      "Soba",
      "Zango-Kataf",
      "Zaria"
    ],
    "Kano": [
      "Ajingi",
      "Albasu",
      "Bagwai",
      "Bebeji",
      "Bichi",
      "Bunkure",
      "Dala",
      "Dambatta",
      "Dawakin Kudu",
      "Dawakin Tofa",
      "Doguwa",
      "Fagge",
      "Gabasawa",
      "Garko",
      "Garum",
      "Mallam",
      "Gaya",
      "Gezawa",
      "Gwale",
      "Gwarzo",
      "Kabo",
      "Kano Municipal",
      "Karaye",
      "Kibiya",
      "Kiru",
      "kumbotso",
      "Kunchi",
      "Kura",
      "Madobi",
      "Makoda",
      "Minjibir",
      "Nasarawa",
      "Rano",
      "Rimin Gado",
      "Rogo",
      "Shanono",
      "Sumaila",
      "Takali",
      "Tarauni",
      "Tofa",
      "Tsanyawa",
      "Tudun Wada",
      "Ungogo",
      "Warawa",
      "Wudil"
    ],
    "Katsina": [
      "Bakori",
      "Batagarawa",
      "Batsari",
      "Baure",
      "Bindawa",
      "Charanchi",
      "Dandume",
      "Danja",
      "Dan Musa",
      "Daura",
      "Dutsi",
      "Dutsin-Ma",
      "Faskari",
      "Funtua",
      "Ingawa",
      "Jibia",
      "Kafur",
      "Kaita",
      "Kankara",
      "Kankia",
      "Katsina",
      "Kurfi",
      "Kusada",
      "Mai'Adua",
      "Malumfashi",
      "Mani",
      "Mashi",
      "Matazuu",
      "Musawa",
      "Rimi",
      "Sabuwa",
      "Safana",
      "Sandamu",
      "Zango"
    ],
    "Kebbi": [
      "Aleiro",
      "Arewa-Dandi",
      "Argungu",
      "Augie",
      "Bagudo",
      "Birnin Kebbi",
      "Bunza",
      "Dandi",
      "Fakai",
      "Gwandu",
      "Jega",
      "Kalgo",
      "Koko/Besse",
      "Maiyama",
      "Ngaski",
      "Sakaba",
      "Shanga",
      "Suru",
      "Wasagu/Danko",
      "Yauri",
      "Zuru"
    ],
    "Kogi": [
      "Adavi",
      "Ajaokuta",
      "Ankpa",
      "Bassa",
      "Dekina",
      "Ibaji",
      "Idah",
      "Igalamela-Odolu",
      "Ijumu",
      "Kabba/Bunu",
      "Kogi",
      "Lokoja",
      "Mopa-Muro",
      "Ofu",
      "Ogori/Mangongo",
      "Okehi",
      "Okene",
      "Olamabolo",
      "Omala",
      "Yagba East",
      "Yagba West"
    ],
    "Kwara": [
      "Asa",
      "Baruten",
      "Edu",
      "Ekiti",
      "Ifelodun",
      "Ilorin East",
      "Ilorin West",
      "Irepodun",
      "Isin",
      "Kaiama",
      "Moro",
      "Offa",
      "Oke-Ero",
      "Oyun",
      "Pategi"
    ],
    "Lagos": [
      "Agege",
      "Ajeromi-Ifelodun",
      "Alimosho",
      "Amuwo-Odofin",
      "Apapa",
      "Badagry",
      "Epe",
      "Eti-Osa",
      "Ibeju/Lekki",
      "Ifako-Ijaye",
      "Ikeja",
      "Ikorodu",
      "Kosofe",
      "Lagos Island",
      "Lagos Mainland",
      "Mushin",
      "Ojo",
      "Oshodi-Isolo",
      "Shomolu",
      "Surulere"
    ],
    "Nasarawa": [
      "Akwanga",
      "Awe",
      "Doma",
      "Karu",
      "Keana",
      "Keffi",
      "Kokona",
      "Lafia",
      "Nasarawa",
      "Nasarawa-Eggon",
      "Obi",
      "Toto",
      "Wamba"
    ],
    "Niger": [
      "Agaie",
      "Agwara",
      "Bida",
      "Borgu",
      "Bosso",
      "Chanchaga",
      "Edati",
      "Gbako",
      "Gurara",
      "Katcha",
      "Kontagora",
      "Lapai",
      "Lavun",
      "Magama",
      "Mariga",
      "Mashegu",
      "Mokwa",
      "Muya",
      "Pailoro",
      "Rafi",
      "Rijau",
      "Shiroro",
      "Suleja",
      "Tafa",
      "Wushishi"
    ],
    "Ogun": [
      "Abeokuta North",
      "Abeokuta South",
      "Ado-Odo/Ota",
      "Egbado North",
      "Egbado South",
      "Ewekoro",
      "Ifo",
      "Ijebu East",
      "Ijebu North",
      "Ijebu North East",
      "Ijebu Ode",
      "Ikenne",
      "Imeko-Afon",
      "Ipokia",
      "Obafemi-Owode",
      "Ogun Waterside",
      "Odeda",
      "Odogbolu",
      "Remo North",
      "Shagamu"
    ],
    "Ondo": [
      "Akoko North East",
      "Akoko North West",
      "Akoko South Akure East",
      "Akoko South West",
      "Akure North",
      "Akure South",
      "Ese-Odo",
      "Idanre",
      "Ifedore",
      "Ilaje",
      "Ile-Oluji",
      "Okeigbo",
      "Irele",
      "Odigbo",
      "Okitipupa",
      "Ondo East",
      "Ondo West",
      "Ose",
      "Owo"
    ],
    "Osun": [
      "Aiyedade",
      "Aiyedire",
      "Atakumosa East",
      "Atakumosa West",
      "Boluwaduro",
      "Boripe",
      "Ede North",
      "Ede South",
      "Egbedore",
      "Ejigbo",
      "Ife Central",
      "Ife East",
      "Ife North",
      "Ife South",
      "Ifedayo",
      "Ifelodun",
      "Ila",
      "Ilesha East",
      "Ilesha West",
      "Irepodun",
      "Irewole",
      "Isokan",
      "Iwo",
      "Obokun",
      "Odo-Otin",
      "Ola-Oluwa",
      "Olorunda",
      "Oriade",
      "Orolu",
      "Osogbo"
    ],
    "Oyo": [
      "Afijio",
      "Akinyele",
      "Atiba",
      "Atigbo",
      "Egbeda",
      "Ibadan Central",
      "Ibadan North",
      "Ibadan North West",
      "Ibadan South East",
      "Ibadan South West",
      "Ibarapa Central",
      "Ibarapa East",
      "Ibarapa North",
      "Ido",
      "Irepo",
      "Iseyin",
      "Itesiwaju",
      "Iwajowa",
      "Kajola",
      "Lagelu Ogbomosho North",
      "Ogbmosho South",
      "Ogo Oluwa",
      "Olorunsogo",
      "Oluyole",
      "Ona-Ara",
      "Orelope",
      "Ori Ire",
      "Oyo East",
      "Oyo West",
      "Saki East",
      "Saki West",
      "Surulere"
    ],
    "Plateau": [
      "Barikin Ladi",
      "Bassa",
      "Bokkos",
      "Jos East",
      "Jos North",
      "Jos South",
      "Kanam",
      "Kanke",
      "Langtang North",
      "Langtang South",
      "Mangu",
      "Mikang",
      "Pankshin",
      "Qua'an Pan",
      "Riyom",
      "Shendam",
      "Wase"
    ],
    "Rivers": [
      "Abua/Odual",
      "Ahoada East",
      "Ahoada West",
      "Akuku Toru",
      "Andoni",
      "Asari-Toru",
      "Bonny",
      "Degema",
      "Emohua",
      "Eleme",
      "Etche",
      "Gokana",
      "Ikwerre",
      "Khana",
      "Obia/Akpor",
      "Ogba/Egbema/Ndoni",
      "Ogu/Bolo",
      "Okrika",
      "Omumma",
      "Opobo/Nkoro",
      "Oyigbo",
      "Port-Harcourt",
      "Tai"
    ],
    "Sokoto": [
      "Binji",
      "Bodinga",
      "Dange-shnsi",
      "Gada",
      "Goronyo",
      "Gudu",
      "Gawabawa",
      "Illela",
      "Isa",
      "Kware",
      "kebbe",
      "Rabah",
      "Sabon birni",
      "Shagari",
      "Silame",
      "Sokoto North",
      "Sokoto South",
      "Tambuwal",
      "Tqngaza",
      "Tureta",
      "Wamako",
      "Wurno",
      "Yabo"
    ],
    "Taraba": [
      "Ardo-kola",
      "Bali",
      "Donga",
      "Gashaka",
      "Cassol",
      "Ibi",
      "Jalingo",
      "Karin-Lamido",
      "Kurmi",
      "Lau",
      "Sardauna",
      "Takum",
      "Ussa",
      "Wukari",
      "Yorro",
      "Zing"
    ],
    "Yobe": [
      "Bade",
      "Bursari",
      "Damaturu",
      "Fika",
      "Fune",
      "Geidam",
      "Gujba",
      "Gulani",
      "Jakusko",
      "Karasuwa",
      "Karawa",
      "Machina",
      "Nangere",
      "Nguru Potiskum",
      "Tarmua",
      "Yunusari",
      "Yusufari"
    ],
    "Zamfara": [
      "Anka",
      "Bakura",
      "Birnin Magaji",
      "Bukkuyum",
      "Bungudu",
      "Gummi",
      "Gusau",
      "Kaura",
      "Namoda",
      "Maradun",
      "Maru",
      "Shinkafi",
      "Talata Mafara",
      "Tsafe",
      "Zurmi"
    ]
};
//];

var states = [
    "Abia",
    "Adamawa",
    "Anambra",
    "Akwa Ibom",
    "Bauchi",
    "Bayelsa",
    "Benue",
    "Borno",
    "Cross River",
    "Delta",
    "Ebonyi",
    "Enugu",
    "Edo",
    "Ekiti",
    "FCT-Abuja",
    "Gombe",
    "Imo",
    "Jigawa",
    "Kaduna",
    "Kano",
    "Katsina",
    "Kebbi",
    "Kogi",
    "Kwara",
    "Lagos",
    "Nasarawa",
    "Niger",
    "Ogun",
    "Ondo",
    "Osun",
    "Oyo",
    "Plateau",
    "Rivers",
    "Sokoto",
    "Taraba",
    "Yobe",
    "Zamfara"
];

function getRootUri() {
    if (window.location.protocol === 'http:') {
        return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port === "" ? "8080" : document.location.port);
    } else {
        return "wss://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port === "" ? "8080" : document.location.port);
    }
}

function init() {  
	websocket = new WebSocket(wsUri);
        
	websocket.onopen = function(evt) {
            __connected === true;
            //websocket.send("conn=connection&test=test data");     
            websocket.send("selectElemNums=selectElemNums" + '&flag=GRPN');
            //msg_to_client = 'Connected to: ' + evt.currentTarget.URL;
            //displayMessage(msg_to_client);
	};

	websocket.onmessage = function(evt) { 
            msg_to_client = evt.data;
            //displayMessage(msg_to_client);
            if(msg_to_client === ""){
                msg_to_client = 'No message from server';                
                displayMessage(msg_to_client);
            }else if(msg_to_client.includes("close")){      
                websocket.close(); 
            } else if(msg_to_client.indexOf("logged in", 0) !== -1){
                if(msg_to_client.includes("logged in")) {
                    $('#login-section').hide(); 
                    $('#groups-section').show(); 
                    $('#officers-section').show(); 
                    $('#aggregations-section').show(); 
                    $('#farmers-section').show(); 
                    $('#farms-section').show(); 
                    $('#farminputs-section').show(); 
                    $('#loans-section').show(); 
                    $('#loan-repayment-section').show();
                    displayMessage("You are logged in"); 
                } else if(msg_to_client.includes("not logged in")) {
                    displayMessage("You are not logged in");
                }
            } else if (msg_to_client.indexOf("groupnums", 0) !== -1){                
                processGroupNums(msg_to_client);
                if (group_list_update){
                    processGroupNums(msg_to_client);
                    updateOFFGroup();
                    updateAGGRGroup();
                    updateFMRGroup();
                    updateFINPGroup();
                    updateFARMGroup();
                    updateFLNGroup();  
                    selectElemNums = [];
                    selectElemData = [];                    
                    group_list_update = false;
                    displayMessage("Group list updated!");  
                } else {
                    buildGroup();
                    displayMessage("Log in to access data!");  
                }
            } else if (msg_to_client.indexOf("farmernums(FAMGPNU)", 0) !== -1){
                processFarmerINGNums(msg_to_client);
                configureFAMFarmerNumLists();
            } else if (msg_to_client.indexOf("farmernums(FIPGPNU)", 0) !== -1){     
                processFarmerINGNums(msg_to_client);
                configureFIPFarmerNumLists();
            } else if (msg_to_client.indexOf("farmernums(FLNGPNU)", 0) !== -1){
                processFarmerINGNums(msg_to_client);
                configureLONFarmerNumLists();
            } else if (msg_to_client.indexOf("farmernums(LRPGPNU)", 0) !== -1){
                processFarmerINGNums(msg_to_client);
                configureLREPFarmerNumLists();
            } else if (msg_to_client.indexOf("loannums", 0) !== -1){
                processFarmerLOANNums(msg_to_client);
                configureLREPLoanNumLists();
            } else if (msg_to_client.indexOf("loandetails", 0) !== -1){
                setPaymentStatus(msg_to_client);
            }else{                            
                displayMessage(msg_to_client); 
            }
	};
        
	websocket.onerror = function(evt) {         
            msg_to_client = 'An error occured. <br/> Reason -> ' + evt.reason;
            displayMessage(msg_to_client);
	};

	websocket.onclose = function(evt) {
            __connected = false;
            var evtcodereason;
            if (evt.wasClean) {
                evtcodereason = "[close] Connection closed cleanly: code -> " + evt.code + " reason -> " + evt.reason;
                evtcodereason += ". Refresh current page to re-establish connection.";
                msg_to_client = evtcodereason;
                displayMessage(msg_to_client);
                not_cleanly_closed = false;
            } else {
                not_cleanly_closed = true;
                // e.g. server process killed or network down
                // event.code is usually 1006 in this case
                msg_to_client = "socket closed: Connection died";
                displayMessage(msg_to_client);
            }
	};
        
        //closeBtn.onclick = function(e) {
        //    e.preventDefault();
        //    websocket.send("close|WSCL");
        //    return false;
        //};
               
        $('#membprofileform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=MSPF';
            var datastr = "";
            var membname = $("#membname").val();            
            var emailphone = $("#emailphone").val();
            var domesticState = $("#domesticState option:selected").text();
            var currentState = $("#currentState option:selected").text();
            var inputCity = $("#inputCity option:selected").text();   
            var inputZip = $("#inputZip").val();
            datastr += "membname=" + membname + "&emailphone" + emailphone + "&domesticState=" + domesticState + "&";
            datastr += "currentState=" + currentState + "&inputCity=" + inputCity + "&dinputZip=" + inputZip + "&";
            var password = $("#password").val();
            datastr += "password=" + password + "&";
            var confirmpassword = $("#confirmpassword").val();
            datastr += "confirmpassword=" + confirmpassword + "&";
            /*
            var social_media = "";
            //$('input[type='checkbox']:checked').each(function () {
            $('input[name="social_media"]:checked').each(function () {
                social_media += this.value + "|";
            });
            datastr += "social_media=" + social_media + "&";
            */
            var picturestr = photo_base64str;
            
            /*
            if ( ! window.FileReader ) {
                //return alert( 'FileReader API is not supported by your browser.' );
                modal.style.display = "block";
                //document.getElementById("sho_message").innerHTML = "<br/>" + text;
                document.getElementById("msg_para").innerHTML = "FileReader API is not supported by your browser.";
            } else { 
                var $i = $( '#inputFileToLoad' ), // Put file input ID here
			input = $i[0]; // Getting the element from jQuery
                if ( input.files && input.files[0] ) {
                    var file = input.files[0]; // The file
                    var fr = new FileReader(); // FileReader instance
                    fr.onload = function () {
			// Do stuff on onload, use fr.result for contents of file
			//$( '#file-content' ).append( $( '<div/>' ).html(fr.result));
                        picturestr = fr.result;                        
                        modal.style.display = "block";
                        //document.getElementById("sho_message").innerHTML = "<br/>" + text;
                        document.getElementById("msg_para").innerHTML = fr.result;
                    };                    
                    // To read as text... uncomment //fr.readAsText(file); line and comment fr.readAsDataURL(file);
                    fr.readAsText( file );
                    //fr.readAsDataURL( file );
                    // fr.readAsDataURL( file )read the file data in Base64 format (also the file content-type (MIME), text/plain, image/jpg, etc)
                } else {
                    // Handle errors here
                    //alert( "File not selected or browser incompatible." )
                    modal.style.display = "block";
                    //document.getElementById("sho_message").innerHTML = "<br/>" + text;
                    document.getElementById("msg_para").innerHTML = "File not selected or browser incompatible.";
                }                
            }
            */
    
            datastr += "picturestr=" + picturestr + '&flag=MSPF';
            websocket.send(datastr);
            return false;    
        });
        
        $('#loginform').submit(function() {
            var datastr = "";
            var lgnnphonenum = $("#lgin-phonenum").val();
            datastr += "lgnnphonenum=" + lgnnphonenum + "&";
            var lgnpassword = $("#lgin-password").val();
            datastr += "lgnpassword=" + lgnpassword + "&flag=LGIN";
            websocket.send(datastr);
            return false;
        });        
                      
        $('#groupform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=GROP';
            var datastr = "";             
            var grpname = $("#grp-name").val(); 
            //var grpidname = $("#grp-groupnum option:selected").text(); 
            //var pt1 = grpidname.indexOf("/", 0);
            //var groupid = grpidname.substring(0, pt1);
            //var grpname = grpidname.substring(pt1 + 1);            
            // capitaliseFirstLetter(str)
            var grpaddress = $("#grp-address").val(); 
            var grpward = $("#grp-ward").val();
	    var grplgvtarea = $("#grp-lgvtarea option:selected").text(); 
	    var grpstate = $("#grp-state option:selected").text(); 
            //var selectedcrop = new Array();
            var selectedcrops = "";
            //var i = 0;
            
            /*
            var selectedcrop = new Array();
            $('input[name="crop-handled"]:checked').each(function() {
                selectedcrop.push(this.value);
            });
            */
            
            /*
            var checkboxes = document.getElementsByName('crop-handled');  
            for (var checkbox of checkboxes) {    
                if (checkbox.checked) {    
                    document.body.append(checkbox.value + ' ');
                    selectedcrop[i] = checkbox.value;
                    i++;
                }
            }
            */
    
            $.each($("input[name='crop-handled']:checked"), function(){
                //selectedcrop.push(this.value);
                selectedcrops += this.value + ", ";
                //i++;
            });
            //selectedcrops = selectedcrops.toString();
            datastr += "grpname=" + grpname + "&grpaddress=" + grpaddress + "&grpward=" + grpward + "&";
	    datastr += "grplgvtarea=" + grplgvtarea + "&grpstate=" + grpstate + "&";
            datastr += "selectedcrops=" + selectedcrops + '&flag=GROP';
            websocket.send(datastr);
            new_group = true;
           return false;    
        });
        
        $('#officerform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=current_username';
            // ghdhdhdh.toUpperCase()
            var datastr = "";
            var firstname = $("#off-firstname").val();
            var middlename = $("#off-middlename").val(); 
            if((middlename === "" )|| (middlename === null) || (middlename === undefined)){
                middlename = "xxx";
            }
            var lastname = $("#off-lastname").val(); 
            var office = $("#off-office").val(); 
            var phonenum = $("#off-phonenum").val();
            //var groupnum = $("#off-group option:selected").text();
            var offgrpidname = $("#off-groupnum option:selected").text(); 
            var pt1 = offgrpidname.indexOf("/", 0);
            var groupid = offgrpidname.substring(0, pt1);
            //var offgrpname = offidname.substring(pt1 + 1);            
            var bvn = $("#off-bvn").val();
            var nim = $("#off-nim").val();
            var homeaddress = $("#off-homeaddress").val();
            datastr += "firstname=" + firstname + "&middlename=" + middlename + "&lastname=" + lastname + "&";
            datastr += "office=" + office + "&phonenum=" + phonenum + "&bvn=" + bvn + "&nim=" + nim + "&";
            datastr += "homeaddress=" + homeaddress + "&groupid=" + groupid +  '&flag=OFFI';
            websocket.send(datastr);            
            return false;    
        });
        
        $('#aggregationform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=AGGR';
            var datastr = "";            
            var aggrtown = $("#aggr-town").val(); 
            var aggrstate = $("#aggr-state option:selected").text();  
            var aggrlocalgvt = $("#aggr-localgvt option:selected").text(); 
            var aggrward = $("#aggr-ward").val();
            
            //var aggrgroup = $("#aggr-group option:selected").text();
            var aggrgrpidname = $("#aggr-groupnum option:selected").text(); 
            var pt1 = aggrgrpidname.indexOf("/", 0);
            var groupid = aggrgrpidname.substring(0, pt1);            
            var grpdetails = aggrgrpidname.substring(pt1 + 1); 
            var nextlevel = $("#aggr-nextlevel").val();  
            datastr += "aggrtown=" + aggrtown + "&aggrward=" + aggrward + "&aggrlocalgvt=" + aggrlocalgvt + "&";
            datastr += "aggrstate=" + aggrstate + "&groupid=" + groupid + "&nextlevel=" + nextlevel +  "&";
            datastr += "grpdetails=" + grpdetails + '&flag=AGGR';  
            websocket.send(datastr);            
            return false;    
        });
        
        $('#farmerform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=FAMR';
            var datastr = "";
            var firstname = $("#fmr-firstname").val();
            var middlename = $("#fmr-middlename").val(); 
            if((middlename === "" )|| (middlename === null) || (middlename === undefined)){
                middlename = "xxx";
            }
            var lastname = $("#fmr-lastname").val();
            var state = $("#fmr-state option:selected").text();
            var localgvt = $("#fmr-localgvt option:selected").text(); 
            var ward = $("#fmr-ward").val();
            var homeaddress = $("#fmr-homeaddress").val();
            var phonenum = $("#fmr-phonenum").val();
            //var groupnum = $("#fmr-groupnum option:selected").text();            
            var bvn = $("#fmr-bvn").val();
            var nim = $("#fmr-nim").val();
            var fmrgrpidname = $("#fmr-groupnum option:selected").text(); 
            var pt1 = fmrgrpidname.indexOf("/", 0);
            var groupid = fmrgrpidname.substring(0, pt1);
            var grpdetails = fmrgrpidname.substring(pt1 + 1);             
            datastr += "firstname=" + firstname + "&middlenamee=" + middlename + "&lastname=" + lastname + "&";
            datastr += "state=" + state + "&localgvt=" + localgvt + "&ward=" + ward + "&homeaddress=" + homeaddress + "&";
            datastr += "phonenum=" + phonenum + "&bvn=" + bvn + "&nim=" + nim + "&groupid=" + groupid + "&";
            datastr += "grpdetails=" + grpdetails + '&flag=FAMR'; 
            websocket.send(datastr);  
            return false;    
        });
        
        $('#farmform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=FARM';
            var datastr = "";
            var longi1 = $("#farm-longi1").val();
            var latit1 = $("#farm-latit1").val();
            var longi2 = $("#farm-longi2").val();
            var latit2 = $("#farm-latit2").val();
            var longi3 = $("#farm-longi3").val(); 
            var latit3 = $("#farm-latit3").val(); 
            var longi4 = $("#farm-longi4").val(); 
            var latit4 = $("#farm-latit4").val(); 
            var farmgrpidname = $("#farm-groupnum option:selected").text();         
            var pt1 = farmgrpidname.indexOf("/", 0);
            var groupid = farmgrpidname.substring(0, pt1);
            //var offgrpname = offidname.substring(pt1 + 1);
            var grpdetails = farmgrpidname.substring(pt1 + 1); 
            //var details_array = details.split(", ");            
            var farmeridname = $("#farm-farmernum option:selected").text(); 
            var pt1 = farmeridname.indexOf("/", 0);
            var farmerid = farmeridname.substring(0, pt1);
            //var farmername = farmeridname.substring(pt1 + 1);
            var farmarea = $("#farm-area").val();
            var community = $("#farm-community").val();
            var ward = $("#farm-ward").val();
            var localgvt =  $("#farm-localgvt option:selected").text();  
            var state =  $("#farm-state option:selected").text();  
            var cropplanted = $("#farm-cropplanted").val(); 
            var areaplanted = $("#farm-areaplanted").val(); 
            var cropyear = $("#farm-year").val();
	    var qtyharvested = $("#farm-harvest").val();
            var cropnetworth = $("#farm-networth").val();
            var cropincome = $("#farm-income").val();
            datastr += "longi1=" + longi1 + "&latit1=" + latit1 + "&longi2=" + longi2 + "&latit2=" + latit2 + "&";
            datastr += "longi3=" + longi3 + "&latit3=" + latit3 + "&longi4=" + longi4 + "&latit4=" + latit4 + "&";
            datastr += "farmarea=" + farmarea + "&community=" + community + "&ward=" + ward + "&localgvt=" + localgvt + "&state=" + state + "&";
            datastr += "cropplanted=" + cropplanted + "&areaplanted=" + areaplanted + "&cropyear=" + cropyear + "&cropharvested=" + qtyharvested + "&";
            datastr += "cropnetworth=" + cropnetworth + "&cropincome=" + cropincome + "&farmerid=" + farmerid + "&";
            datastr += "grpdetails=" + grpdetails + '&flag=FARM'; 
            websocket.send(datastr);            
            return false;    
        });
        
        $('#farminputform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=MSPF';
            var datastr = "";
            var finpname = $("#finp-name").val(); 
	    var finpcost = $("#finp-cost").val();
            var finpyear = $("#finp-year").val();
            //var groupnum = $("#finp-groupnum option:selected").text();		
            //var farmernum = $("#finp-farmernum option:selected").text();
            var finpgrpidname = $("#finp-groupnum option:selected").text();         
            var pt1 = finpgrpidname.indexOf("/", 0);
            var groupid = finpgrpidname.substring(0, pt1);
            //var offgrpname = offidname.substring(pt1 + 1);
            
            var finpfmridname = $("#finp-farmernum option:selected").text(); 
            var pt1 = finpfmridname.indexOf("/", 0);
            var farmerid = finpfmridname.substring(0, pt1);
            //var finpfmrname = finpfmridname.substring(pt1 + 1);
            datastr += "finpname=" + finpname + "&finpcost=" + finpcost + "&finpyear=" + finpyear + "&";
            datastr += "farmerid=" + farmerid + '&flag=FINP';
            websocket.send(datastr);
            return false;    
        });
        
        $('#loanform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=MSPF';
            var datastr = "";
            var flndesc = $("#fln-desc").val();
            var flnloan = $("#fln-loan").val();
            var flnpayment = $("#fln-payment").val();
            if((flnpayment === "" )|| (flnpayment === null) || (flnpayment === undefined)){
                flnpayment = "0.00";
            }
            //var groupnum = $("#fln-groupnum option:selected").text();
            //var farmernum = $("#fln-farmernum option:selected").text();
            var flngrpidname = $("#fln-groupnum option:selected").text();         
            var pt1 = flngrpidname.indexOf("/", 0);
            var groupid = flngrpidname.substring(0, pt1);
            //var offgrpname = offidname.substring(pt1 + 1);
            
            var flnfmridname = $("#fln-farmernum option:selected").text(); 
            var pt1 = flnfmridname.indexOf("/", 0);
            var farmerid = flnfmridname.substring(0, pt1);
            //var finpfmrname = finpfmridname.substring(pt1 + 1);
            datastr += "flndesc=" + flndesc + "&flnloan=" + flnloan + "&flnpayment=" + flnpayment + "&";
            datastr += "farmerid=" + farmerid + '&flag=FLON';
            websocket.send(datastr);
            return false;    
        });
        
        $('#loanrepaymentform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=MSPF';
            var datastr = "";
            //var flrepdesc = $("#lrep-desc").val();            
            //var groupnum = $("#fln-groupnum option:selected").text();
            //var farmernum = $("#fln-farmernum option:selected").text();
            //var flnrepgrpidname = $("#lrep-groupnum option:selected").text();         
            //var pt1 = flnrepgrpidname.indexOf("/", 0);
            //var groupid = flnrepgrpidname.substring(0, pt1);
            //var offgrpname = offidname.substring(pt1 + 1);
            
            //var flnrepfmridname = $("#lrep-farmernum option:selected").text(); 
            //pt1 = flnrepfmridname.indexOf("/", 0);
            //var farmerid = flnrepfmridname.substring(0, pt1);
            
            var flnreplniddesc = $("#lrep-loannum option:selected").text();
            var pt1 = flnreplniddesc.indexOf("/", 0);
            var loanid = flnreplniddesc.substring(0, pt1);
            var flreploan = $("#lrep-loan").val();
            var flrepamountpaid = $("#lrep-amountpaid").val();
            var flrepnewpayment = $("lrep-newpayment").val();
            if((flrepnewpayment === "" )|| (flrepnewpayment === null) || (flrepnewpayment === undefined)){
                flrepnewpayment = "0.00";
            }
            //var finpfmrname = finpfmridname.substring(pt1 + 1);
            datastr += "flreploan=" + flreploan + "&flrepamountpaid=" + flrepamountpaid + "&";
            datastr += "flrepnewpayment=" + flrepnewpayment + "&loanid=" + loanid + '&flag=FLNREP';
            websocket.send(datastr);
            return false;    
        });
        
        $('#facebook-button').on('click', function() {
            FB.login(function (response) {
                if (response.authResponse) {
                    // Get and display the user profile data
                    //getFbUserData();
                    FB.api('/me', {locale: 'en_US', fields: 'id,first_name,last_name,email,link,gender,locale,picture'},
                    function (response) {
                        //document.getElementById('fbLink').setAttribute("onclick","fbLogout()");
                        //document.getElementById('fbLink').innerHTML = 'Logout from Facebook';
                        //document.getElementById('status').innerHTML = '<p>Thanks for logging in, ' + response.first_name + '!</p>';
                        document.getElementById('userData').innerHTML = '<h2>Facebook Profile Details</h2><p><img src="'+response.picture.data.url+'"/></p><p><b>FB ID:</b> '+response.id+'</p><p><b>Name:</b> '+response.first_name+' '+response.last_name+'</p><p><b>Email:</b> '+response.email+'</p><p><b>Gender:</b> '+response.gender+'</p><p><b>FB Profile:</b> <a target="_blank" href="'+response.link+'">click to view profile</a></p>';
                        msg_to_client = '<h2>Facebook Profile Details</h2><p><img src="'+response.picture.data.url+'"/></p><p><b>FB ID:</b> '+response.id+'</p><p><b>Name:</b> '+response.first_name+' '+response.last_name+'</p><p><b>Email:</b> '+response.email+'</p><p><b>Gender:</b> '+response.gender+'</p><p><b>FB Profile:</b> <a target="_blank" href="'+response.link+'">click to view profile</a></p>';
                        displayMessage(msg_to_client);
                    });
                } else {
                    //document.getElementById('status').innerHTML = 'User cancelled login or did not fully authorize.';
                    msg_to_client = "User cancelled login or did not fully author";
                    displayMessage(msg_to_client);
                }
            }, {scope: 'email'});
            
        });
        
        $('#twitter-button').on('click', function() {
            // Initialize with your OAuth.io app public key
            OAuth.initialize('HwAr2OtSxRgEEnO2-JnYjsuA3tc');
            // Use popup for OAuth
            OAuth.popup('twitter').then(twitter => {
                console.log('twitter:', twitter);
                // Prompts 'welcome' message with User's email on successful login
                // #me() is a convenient method to retrieve user data without requiring you
                // to know which OAuth provider url to call
                twitter.me().then(data => {
                    console.log('data:', data);
                    alert('Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details");
                    //var msg_to_client = 'Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details";
                    //displayMessage(msg_to_client);
                });
                // Retrieves user data from OAuth provider by using #get() and
                // OAuth provider url    
                twitter.get('/1.1/account/verify_credentials.json?include_email=true').then(data => {
                    console.log('self data:', data);
                    //var msg_to_client = 'Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details";
                    //displayMessage(msg_to_client);
                });    
            });
        });
        
        $('#instagram-button').on('click', function() {
            var instagramClientId = '16edb5c3bc05437594d69178f2aa646a';        
            var instagramRedirectUri = 'localhost/faceboo';
            var popupWidth = 700,
                popupHeight = 500,
                popupLeft = (window.screen.width - popupWidth) / 2,
                popupTop = (window.screen.height - popupHeight) / 2;
            // Url needs to point to instagram_auth.php
            var popup = window.open('instagram.html', '', 'width='+popupWidth+',height='+popupHeight+',left='+popupLeft+',top='+popupTop+'');
            //var popup = window.open('instagram_auth.php', '', 'width='+popupWidth+',height='+popupHeight+',left='+popupLeft+',top='+popupTop+'');
            popup.onload = function() {
                // Open authorize url in pop-up
                if(window.location.hash.length === 0) {
                    popup.open('https://instagram.com/oauth/authorize/?client_id='+instagramClientId+'&redirect_uri='+instagramRedirectUri+'&response_type=token', '_self');
                }
                // An interval runs to get the access token from the pop-up
                var interval = setInterval(function() {
                    try {
                        // Check if hash exists
                        if(popup.location.hash.length) {
                            // Hash found, that includes the access token
                            clearInterval(interval);
                            accessToken = popup.location.hash.slice(14); //slice #access_token= from string
                            popup.close();
                            //if(callback !== undefined && typeof callback === 'function'){
                            //    callback();
                            //}
                            //alert("You are successfully logged in! Access Token: "+accessToken);
                            $.ajax({
                                type: "GET",
                                dataType: "jsonp",
                                url: "https://api.instagram.com/v1/users/self/?access_token="+accessToken,
                                success: function(response){
                                    // Change button and show status
                                    $('.instagramLoginBtn').attr('onclick','instagramLogout()');
                                    $('.btn-text').text('Logout from Instagram');
                                    $('#status').text('Thanks for logging in, ' + response.data.username + '!');

                                    // Display user data
                                    msg_to_client = '<p><b>Instagram ID:</b> '+response.data.id+'</p><p><b>Name:</b> '+response.data.full_name+'</p><p><b>Picture:</b> <img src="'+response.data.profile_picture+'"/></p><p><b>Instagram Profile:</b> <a target="_blank" href="https://www.instagram.com/'+response.data.username+'">click to view profile</a></p>';
                                    //displayUserProfileData(response.data);
                                    displayMessage(msg_to_client);
                                    // Save user data
                                    //saveUserData(response.data);
                                }
                            });
                        }
                    }
                    catch(evt) {
                        // Permission denied
                    }
                }, 100);
            };
        });
        
        //requestselectElemNums();
        //buildGroup();
}

//function requestselectElemNums(){
//    var reqstr = "selectElemNums=selectElemNums" + '&flag=GRPN';
//    websocket.send(reqstr);  
//}

var selectElemNums = [];
var selectElemData = [];    
var groupnum;

function processGroupNums(msg_to_client){
    // "selectElemNums]2112/assoc 1#1211/assoc 2"
    var pt = msg_to_client.indexOf("]", 0);
    pt++;
    var nums_details = msg_to_client.substring(pt);
    var numinfo_array = strToArray(nums_details);
    
    selectElemNums[0] = "nap";
    selectElemData[0] = "Group# .......";
    for(var i=0;i<numinfo_array.length;i++){
        if ((numinfo_array[i].indexOf("record saved", 0) === -1) && (numinfo_array[i].indexOf("unknown", 0) === -1)){
            var pt1 = numinfo_array[i].indexOf("/", 0);
            selectElemData[i+1] = numinfo_array[i].substring(0, pt1);
            selectElemNums[i+1] = numinfo_array[i];
        }
    }
}

function processFarmerINGNums(msg_to_client){
    // "selectElemNums]2112/assoc 1#1211/assoc 2"
    var pt = msg_to_client.indexOf("]", 0);
    pt++;
    var nums_details = msg_to_client.substring(pt);
    var numinfo_array = strToArray(nums_details);
    
    selectElemNums[0] = "nap";
    selectElemData[0] = "Farmer# .......";
    for(var i=0;i<numinfo_array.length;i++){
        var pt1 = numinfo_array[i].indexOf("/", 0);
        selectElemData[i+1] = numinfo_array[i].substring(0, pt1);
        selectElemNums[i+1] = numinfo_array[i];
    }
}

function processFarmerLOANNums(msg_to_client){
    // "selectElemNums]2112/assoc 1#1211/assoc 2"
    var pt = msg_to_client.indexOf("]", 0);
    pt++;
    var nums_details = msg_to_client.substring(pt);
    var numinfo_array = strToArray(nums_details);
    
    selectElemNums[0] = "nap";
    selectElemData[0] = "Loan# .......";
    for(var i=0;i<numinfo_array.length;i++){
        var pt1 = numinfo_array[i].indexOf("/", 0);
        selectElemData[i+1] = numinfo_array[i].substring(0, pt1);
        selectElemNums[i+1] = numinfo_array[i];
    }
}

function farmFmrInGroup(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var groupid = selectObj.options[idx].value; 
    //retreiveFAMGRPNums(groupid);
    var reqstr = "groupnum=" + groupid + '&flag=FAMGPNU';
    websocket.send(reqstr);
}
        
function farmInputFmrInGroup(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var groupid = selectObj.options[idx].value; 
    //retreiveFIPGRPNums(groupid);
    var reqstr = "groupnum=" + groupid + '&flag=FIPGPNU';
    websocket.send(reqstr);
}       
            
function loanFmrInGroup(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var groupid = selectObj.options[idx].value; 
    //retreiveFLNGRPNums(groupid);
    var reqstr = "groupnum=" + groupid + '&flag=FLNGPNU';
    websocket.send(reqstr);
}

function loanRepFmrInGroup(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var groupid = selectObj.options[idx].value; 
    //retreiveFLNGRPNums(groupid);
    var reqstr = "groupnum=" + groupid + '&flag=LRPGPNU';
    websocket.send(reqstr);
}

function farmerloansList(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var groupid = selectObj.options[idx].value; 
    //retreiveFLNGRPNums(groupid);
    var reqstr = "farmernum=" + groupid + '&flag=FLNFMRNU';
    websocket.send(reqstr);
}

function reteiveLoanStatus(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var loanid = selectObj.options[idx].value; 
    //retreiveFLNGRPNums(groupid);
    var reqstr = "loannum=" + loanid + '&flag=LONDETS';
    websocket.send(reqstr);
}

function retreiveLocalGovts(selectObj) {    
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    //var selectedtext = selectObj.options[idx].text; 
    var state = selectObj.options[idx].value; 
    //for(var i = 0; i < statelgovts.length; i++){
    var lgovtlist = null;
    for(var i = 0; i < states.length; i++){
        if (states[i].includes(state)) {
            lgovtlist = statelgovts[states[i]];
        }
    }   
    
    for(var i=0;i<lgovtlist.length;i++){
        selectElemData[i] = lgovtlist[i];
        selectElemNums[i] = lgovtlist[i];
    }
}

function configureFAMFarmerNumLists() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Farmer# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("farm-farmernum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
    selectElemNums = [];
    selectElemData = [];
}

function configureFIPFarmerNumLists() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Farmer# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("finp-farmernum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
    selectElemNums = [];
    selectElemData = [];
}

function configureLONFarmerNumLists() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Farmer# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("fln-farmernum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
    selectElemNums = [];
    selectElemData = [];
}

function configureLREPFarmerNumLists() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Farmer# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("lrep-farmernum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
    selectElemNums = [];
    selectElemData = [];
}

function configureLREPLoanNumLists() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Loan# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("lrep-loannum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
    selectElemNums = [];
    selectElemData = [];
}

function doc(id){
    return document.getElementById(id);
}

function buildGroup(){
    
    if(selectElemData[i] !== "null"){
        if (selectElemData[i] !== "none") {
            
            var offopts=doc('off-groupnum').options;
            for(var i=1;i<selectElemData.length;i++){
                 offopts[offopts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
                        
            var aggropts=doc('aggr-groupnum').options;
            for(var i=1;i<selectElemData.length;i++){
                 aggropts[aggropts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
            
            var fmropts=doc('fmr-groupnum').options;
            for(var i=1;i<selectElemData.length;i++){
                 fmropts[fmropts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
                        
            var farmopts=doc('farm-groupnum').options;
            for(var i=1;i<selectElemData.length;i++){
                 farmopts[farmopts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
                        
            var finpopts=doc('finp-groupnum').options; 
            for(var i=1;i<selectElemData.length;i++){
                 finpopts[finpopts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
                        
            var flnopts=doc('fln-groupnum').options; 
            for(var i=1;i<selectElemData.length;i++){
                 flnopts[flnopts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
            
            var flnopts=doc('lrep-groupnum').options; 
            for(var i=1;i<selectElemData.length;i++){
                 flnopts[flnopts.length]=new Option(selectElemNums[i],selectElemData[i]);
            }
        }
    }
    
    selectElemNums = [];
    selectElemData = [];  
}

setInterval(checkNewGroupStatus, 3000);

function checkNewGroupStatus(){
    if (new_group){
        websocket.send("selectElemNums=selectElemNums" + '&flag=GRPN');
        group_list_update = true;
        new_group = false;
    }
}

function updateOFFGroup() {
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("off-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    } 
}

function updateFMRGroup() { 
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("fmr-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
}

function updateAGGRGroup() {    
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("aggr-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
}

function updateFARMGroup() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("farm-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
}

function updateFINPGroup() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("finp-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
}

function updateFLNGroup() { 
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Group# .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("fln-groupnum"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
}

//###################################################################################################################
function configureGroupLGVTAS() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Local Government .....";        
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("grp-lgvtarea"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
    
    selectElemNums = [];
    selectElemData = [];
}

function configureFarmerLGVTAS() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Local Government .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("fmr-localgvt"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
    
    selectElemNums = [];
    selectElemData = [];
}

function configureAggregationLGVTAS() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Local Government .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("aggr-localgvt"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
    
    selectElemNums = [];
    selectElemData = [];
}

function configureFarmLGVTAS() {        
    var cSVals = [];
    var cSTxts = [];   
    var k = 0;
    cSVals[k] = "nap";
    cSTxts[k] = "Local Government .....";
    k++;
    for (i = 0; i < selectElemNums.length; i++) {
        if(selectElemNums[i] !== "null"){
            cSVals[k] = selectElemData[i];
            cSTxts[k] = selectElemNums[i];
            k++;
        }
    }
    // get the country select element via its known id 
    var cSelect = document.getElementById("farm-localgvt"); 
    // remove the current options from the country select 
    while (cSelect.options.length > 0) { 
        cSelect.remove(0); 
    } 
    var newOption; 
    // create new options 
    for (var i=0; i<cSVals.length; i++) { 
        newOption = document.createElement("option"); 
 	newOption.value = cSVals[i];  // assumes option string and value are the same 
 	newOption.text = cSTxts[i]; 
 	// add the new option 
 	try { 
            cSelect.add(newOption);  // this will fail in DOM browsers but is needed for IE 
 	}catch (e) { 
            cSelect.appendChild(newOption); 
 	} 
    }
    
    selectElemNums = [];
    selectElemData = [];
}
//###################################################################################################################

function strToArray(arrayAsString) {
    var realJsString = arrayAsString + "";
    var re=/[#]/;                       
    var arrayFromJava = realJsString.split(re);
    return arrayFromJava;
}
    
window.fbAsyncInit = function() {
    // FB JavaScript SDK configuration and setup
    FB.init({
      appId      : '1119999988888898981989819891', // FB App ID
      cookie     : true,  // enable cookies to allow the server to access the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v9.0' // use graph api version 2.8
    });
    
    // Check whether the user already logged in
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            //display user data
            msg_to_client = 'facebook connected';
            displayMessage(msg_to_client);
            //getFbUserData();
        }
    });
};

setInterval(reconnect_ws, 5000);

var reconnect_ws = function(){ 
    if (not_cleanly_closed) {
        init();
        not_cleanly_closed = false;
    }
};

function encodeImageFileAsURL() {
    var fi = document.getElementById('inputFileToLoad'); // GET THE FILE INPUT.
    var fsize = 0;
    // VALIDATE OR CHECK IF ANY FILE IS SELECTED.
    if (fi.files.length > 0) {
        fsize = fi.files.item(0).size;
        
        /*
        // RUN A LOOP TO CHECK EACH SELECTED FILE.
        for (var i = 0; i <= fi.files.length - 1; i++) {

            var fsize = fi.files.item(i).size;      // THE SIZE OF THE FILE.
            document.getElementById('fp').innerHTML =
                document.getElementById('fp').innerHTML + '<br /> ' +
                    '<b>' + Math.round((fsize / 1024)) + '</b> KB';
        }
        */
        var fileSize = Math.round((fsize / 1024));
        if (fileSize <= 15){     
            modal.style.display = "none";
            var fileToLoad = fi.files[0];
            var fileReader = new FileReader();

            fileReader.onload = function(fileLoadedEvent) {
                var srcData = fileLoadedEvent.target.result; // <--- data: base64

                var newImage = document.createElement('img');
                newImage.src = srcData;

                document.getElementById("imgTest").innerHTML = newImage.outerHTML;
                photo_base64str = document.getElementById("imgTest").innerHTML;
            };
            fileReader.readAsDataURL(fileToLoad);            
        }else{
            msg_to_client = "File size is greater than 15kb.";
            displayMessage(msg_to_client);
        }
    }else{
        msg_to_client = "File is invalid or empty.";
        displayMessage(msg_to_client);
    }
}

function setPaymentStatus(cltmsg) {
    var pt = cltmsg.indexOf("]", 0);
    pt++;
    var details = cltmsg.substring(pt);
    var realJsString = details + "";
    var re=/[|]/;                       
    var values = realJsString.split(re);
    document.getElementById("lrep-loan").value = values[0]; 
    document.getElementById("lrep-amountpaid").value = values[1]; 
}

function displayMessage(cltmsg) { 
     modal.style.display = "block";
     document.getElementById("msg_para").innerHTML = cltmsg;
}

window.addEventListener("load", init, false);       
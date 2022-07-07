package com.catenax.valueaddedservice.web.rest;

import com.catenax.valueaddedservice.dto.DashBoardDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DashBoardResource {

    private final Logger log = LoggerFactory.getLogger(DashBoardResource.class);

    private static final String ENTITY_NAME = "dataSource";

    @Value("${application.host}")
    private String host;

    @GetMapping("/dashboard")
    public ResponseEntity<List<DashBoardDto>> getAllDashBoard(Pageable pageable) throws IOException {
        log.debug("REST request to get a page of Dashboard");
        List<DashBoardDto> dashBoardDtos = new ObjectMapper().readValue("[{\n" +
                        "  \"id\": 1,\n" +
                        "  \"bpn\": \"Camimbo\",\n" +
                        "  \"legalName\": \"Kovacek, Ritchie and Roob\",\n" +
                        "  \"address\": \"6018 Blue Bill Park Avenue\",\n" +
                        "  \"city\": \"Guanzhuang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Graphidaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 2,\n" +
                        "  \"bpn\": \"Janyx\",\n" +
                        "  \"legalName\": \"Walker-Ritchie\",\n" +
                        "  \"address\": \"102 Muir Alley\",\n" +
                        "  \"city\": \"Białobrzegi\",\n" +
                        "  \"country\": \"Poland\",\n" +
                        "  \"score\": 2010,\n" +
                        "  \"rating\": \"Pinaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 3,\n" +
                        "  \"bpn\": \"Divape\",\n" +
                        "  \"legalName\": \"Swift-Torphy\",\n" +
                        "  \"address\": \"8107 Eliot Center\",\n" +
                        "  \"city\": \"Kīsh\",\n" +
                        "  \"country\": \"Iran\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Parmeliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 4,\n" +
                        "  \"bpn\": \"Dynabox\",\n" +
                        "  \"legalName\": \"Keeling, Wiegand and Olson\",\n" +
                        "  \"address\": \"78656 Esker Trail\",\n" +
                        "  \"city\": \"Ketang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2000,\n" +
                        "  \"rating\": \"Scrophulariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 5,\n" +
                        "  \"bpn\": \"Rhynoodle\",\n" +
                        "  \"legalName\": \"Mante, Brekke and Bergnaum\",\n" +
                        "  \"address\": \"14571 Briar Crest Point\",\n" +
                        "  \"city\": \"Agboville\",\n" +
                        "  \"country\": \"Ivory Coast\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Dryopteridaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 6,\n" +
                        "  \"bpn\": \"Skinte\",\n" +
                        "  \"legalName\": \"Predovic Inc\",\n" +
                        "  \"address\": \"20614 5th Lane\",\n" +
                        "  \"city\": \"Okpo\",\n" +
                        "  \"country\": \"Nigeria\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Apiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 7,\n" +
                        "  \"bpn\": \"Shufflebeat\",\n" +
                        "  \"legalName\": \"Dicki-Schimmel\",\n" +
                        "  \"address\": \"8 Prairie Rose Plaza\",\n" +
                        "  \"city\": \"Singabarong\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1994,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 8,\n" +
                        "  \"bpn\": \"Skimia\",\n" +
                        "  \"legalName\": \"Sporer, Conn and Streich\",\n" +
                        "  \"address\": \"36 Canary Alley\",\n" +
                        "  \"city\": \"Pokrzywnica\",\n" +
                        "  \"country\": \"Poland\",\n" +
                        "  \"score\": 1983,\n" +
                        "  \"rating\": \"Rosaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 9,\n" +
                        "  \"bpn\": \"Mynte\",\n" +
                        "  \"legalName\": \"Moore, Abshire and Cummerata\",\n" +
                        "  \"address\": \"97630 Buell Drive\",\n" +
                        "  \"city\": \"Karangmulyo\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Celastraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 10,\n" +
                        "  \"bpn\": \"Avaveo\",\n" +
                        "  \"legalName\": \"Thompson and Sons\",\n" +
                        "  \"address\": \"1101 Moland Circle\",\n" +
                        "  \"city\": \"Riangderi\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 2000,\n" +
                        "  \"rating\": \"Moraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 11,\n" +
                        "  \"bpn\": \"Ainyx\",\n" +
                        "  \"legalName\": \"Wiza and Sons\",\n" +
                        "  \"address\": \"939 Bartelt Road\",\n" +
                        "  \"city\": \"Sogamoso\",\n" +
                        "  \"country\": \"Colombia\",\n" +
                        "  \"score\": 2001,\n" +
                        "  \"rating\": \"Rhamnaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 12,\n" +
                        "  \"bpn\": \"Thoughtsphere\",\n" +
                        "  \"legalName\": \"Abernathy-Grady\",\n" +
                        "  \"address\": \"3297 Morningstar Plaza\",\n" +
                        "  \"city\": \"Digdig\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 13,\n" +
                        "  \"bpn\": \"Fatz\",\n" +
                        "  \"legalName\": \"Rempel, Schoen and Trantow\",\n" +
                        "  \"address\": \"686 Canary Pass\",\n" +
                        "  \"city\": \"Requena\",\n" +
                        "  \"country\": \"Peru\",\n" +
                        "  \"score\": 1996,\n" +
                        "  \"rating\": \"Malvaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 14,\n" +
                        "  \"bpn\": \"Realpoint\",\n" +
                        "  \"legalName\": \"Weber, Block and Botsford\",\n" +
                        "  \"address\": \"21 Jana Way\",\n" +
                        "  \"city\": \"Xiawuqi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Brassicaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 15,\n" +
                        "  \"bpn\": \"Realfire\",\n" +
                        "  \"legalName\": \"Franecki, Schaefer and Hamill\",\n" +
                        "  \"address\": \"73237 Roth Junction\",\n" +
                        "  \"city\": \"Pupiales\",\n" +
                        "  \"country\": \"Colombia\",\n" +
                        "  \"score\": 1993,\n" +
                        "  \"rating\": \"Urticaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 16,\n" +
                        "  \"bpn\": \"Jaxbean\",\n" +
                        "  \"legalName\": \"Stoltenberg, McCullough and Williamson\",\n" +
                        "  \"address\": \"314 Daystar Alley\",\n" +
                        "  \"city\": \"Bacolod\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 17,\n" +
                        "  \"bpn\": \"Jaxworks\",\n" +
                        "  \"legalName\": \"Daniel-Kuphal\",\n" +
                        "  \"address\": \"747 Novick Point\",\n" +
                        "  \"city\": \"Eskilstuna\",\n" +
                        "  \"country\": \"Sweden\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Rubiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 18,\n" +
                        "  \"bpn\": \"Gevee\",\n" +
                        "  \"legalName\": \"Stark, Wiegand and Renner\",\n" +
                        "  \"address\": \"5163 Ludington Trail\",\n" +
                        "  \"city\": \"Pindangan Centro\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 1993,\n" +
                        "  \"rating\": \"Apiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 19,\n" +
                        "  \"bpn\": \"Fivechat\",\n" +
                        "  \"legalName\": \"Hudson-Simonis\",\n" +
                        "  \"address\": \"1 Jenna Alley\",\n" +
                        "  \"city\": \"Taotang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1980,\n" +
                        "  \"rating\": \"Orchidaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 20,\n" +
                        "  \"bpn\": \"Twitterbridge\",\n" +
                        "  \"legalName\": \"Stanton-Nolan\",\n" +
                        "  \"address\": \"431 Green Point\",\n" +
                        "  \"city\": \"Ganxi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2001,\n" +
                        "  \"rating\": \"Rosaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 21,\n" +
                        "  \"bpn\": \"Quatz\",\n" +
                        "  \"legalName\": \"Johnston-Ernser\",\n" +
                        "  \"address\": \"31 Evergreen Junction\",\n" +
                        "  \"city\": \"Tangchi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1987,\n" +
                        "  \"rating\": \"Cucurbitaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 22,\n" +
                        "  \"bpn\": \"Edgepulse\",\n" +
                        "  \"legalName\": \"Roberts Group\",\n" +
                        "  \"address\": \"32940 Mcbride Center\",\n" +
                        "  \"city\": \"Hartford\",\n" +
                        "  \"country\": \"United States\",\n" +
                        "  \"score\": 2003,\n" +
                        "  \"rating\": \"Poaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 23,\n" +
                        "  \"bpn\": \"Tavu\",\n" +
                        "  \"legalName\": \"Smith Inc\",\n" +
                        "  \"address\": \"5 Sheridan Court\",\n" +
                        "  \"city\": \"Qobustan\",\n" +
                        "  \"country\": \"Azerbaijan\",\n" +
                        "  \"score\": 1959,\n" +
                        "  \"rating\": \"Pteridaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 24,\n" +
                        "  \"bpn\": \"Skippad\",\n" +
                        "  \"legalName\": \"Pfannerstill, Tremblay and Predovic\",\n" +
                        "  \"address\": \"2 Bunting Drive\",\n" +
                        "  \"city\": \"Tanahbeureum\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1999,\n" +
                        "  \"rating\": \"Onagraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 25,\n" +
                        "  \"bpn\": \"Eire\",\n" +
                        "  \"legalName\": \"Zboncak-Roberts\",\n" +
                        "  \"address\": \"119 Riverside Alley\",\n" +
                        "  \"city\": \"Qal‘ah-ye Kuhnah\",\n" +
                        "  \"country\": \"Afghanistan\",\n" +
                        "  \"score\": 2005,\n" +
                        "  \"rating\": \"Poaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 26,\n" +
                        "  \"bpn\": \"Jetpulse\",\n" +
                        "  \"legalName\": \"Sauer-Gottlieb\",\n" +
                        "  \"address\": \"74 Schiller Circle\",\n" +
                        "  \"city\": \"Horní Štěpánov\",\n" +
                        "  \"country\": \"Czech Republic\",\n" +
                        "  \"score\": 2002,\n" +
                        "  \"rating\": \"Myrtaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 27,\n" +
                        "  \"bpn\": \"Lazz\",\n" +
                        "  \"legalName\": \"Koch, Bechtelar and Okuneva\",\n" +
                        "  \"address\": \"0419 Clyde Gallagher Pass\",\n" +
                        "  \"city\": \"Zhaoqing\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1994,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 28,\n" +
                        "  \"bpn\": \"Yoveo\",\n" +
                        "  \"legalName\": \"Considine, Jakubowski and Hoeger\",\n" +
                        "  \"address\": \"6 Corscot Lane\",\n" +
                        "  \"city\": \"Malanday\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Cladoniaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 29,\n" +
                        "  \"bpn\": \"Linkbridge\",\n" +
                        "  \"legalName\": \"Blanda, Cole and Schinner\",\n" +
                        "  \"address\": \"31089 Lakewood Park\",\n" +
                        "  \"city\": \"Pajung\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1999,\n" +
                        "  \"rating\": \"Orthotrichaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 30,\n" +
                        "  \"bpn\": \"Zoovu\",\n" +
                        "  \"legalName\": \"Pfannerstill, O'Keefe and Pollich\",\n" +
                        "  \"address\": \"36841 Prairieview Park\",\n" +
                        "  \"city\": \"Wuxue Shi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1995,\n" +
                        "  \"rating\": \"Solanaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 31,\n" +
                        "  \"bpn\": \"Bubbletube\",\n" +
                        "  \"legalName\": \"Lindgren-Hickle\",\n" +
                        "  \"address\": \"2 Green Pass\",\n" +
                        "  \"city\": \"Tianyu\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1991,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 32,\n" +
                        "  \"bpn\": \"Devpoint\",\n" +
                        "  \"legalName\": \"Runte and Sons\",\n" +
                        "  \"address\": \"077 Cambridge Parkway\",\n" +
                        "  \"city\": \"Sölvesborg\",\n" +
                        "  \"country\": \"Sweden\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Lamiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 33,\n" +
                        "  \"bpn\": \"Rhyzio\",\n" +
                        "  \"legalName\": \"Heathcote Inc\",\n" +
                        "  \"address\": \"76 Macpherson Street\",\n" +
                        "  \"city\": \"Keswick\",\n" +
                        "  \"country\": \"Canada\",\n" +
                        "  \"score\": 2012,\n" +
                        "  \"rating\": \"Brassicaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 34,\n" +
                        "  \"bpn\": \"Geba\",\n" +
                        "  \"legalName\": \"Nader Inc\",\n" +
                        "  \"address\": \"1203 Holy Cross Avenue\",\n" +
                        "  \"city\": \"Lysogorskaya\",\n" +
                        "  \"country\": \"Russia\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Lamiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 35,\n" +
                        "  \"bpn\": \"Einti\",\n" +
                        "  \"legalName\": \"Heidenreich, Hahn and Nicolas\",\n" +
                        "  \"address\": \"6 Kenwood Junction\",\n" +
                        "  \"city\": \"Arosbaya\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Brassicaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 36,\n" +
                        "  \"bpn\": \"Oyoba\",\n" +
                        "  \"legalName\": \"Mills, Kilback and Mills\",\n" +
                        "  \"address\": \"89501 Oneill Junction\",\n" +
                        "  \"city\": \"Ganxi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Acarosporaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 37,\n" +
                        "  \"bpn\": \"Vidoo\",\n" +
                        "  \"legalName\": \"Roob, Howe and Hirthe\",\n" +
                        "  \"address\": \"247 Lotheville Drive\",\n" +
                        "  \"city\": \"Ambarakaraka\",\n" +
                        "  \"country\": \"Madagascar\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Lauraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 38,\n" +
                        "  \"bpn\": \"Skalith\",\n" +
                        "  \"legalName\": \"O'Connell Inc\",\n" +
                        "  \"address\": \"516 Kipling Alley\",\n" +
                        "  \"city\": \"Söderhamn\",\n" +
                        "  \"country\": \"Sweden\",\n" +
                        "  \"score\": 1969,\n" +
                        "  \"rating\": \"Grossulariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 39,\n" +
                        "  \"bpn\": \"Dynazzy\",\n" +
                        "  \"legalName\": \"Bosco, Hegmann and Dickens\",\n" +
                        "  \"address\": \"48436 Lakewood Street\",\n" +
                        "  \"city\": \"Linköping\",\n" +
                        "  \"country\": \"Sweden\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Piperaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 40,\n" +
                        "  \"bpn\": \"Photobug\",\n" +
                        "  \"legalName\": \"Corkery and Sons\",\n" +
                        "  \"address\": \"70076 Stoughton Alley\",\n" +
                        "  \"city\": \"Dongshan\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2008,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 41,\n" +
                        "  \"bpn\": \"Blogspan\",\n" +
                        "  \"legalName\": \"Runolfsson-Kiehn\",\n" +
                        "  \"address\": \"5 Springview Court\",\n" +
                        "  \"city\": \"Syców\",\n" +
                        "  \"country\": \"Poland\",\n" +
                        "  \"score\": 2000,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 42,\n" +
                        "  \"bpn\": \"Feednation\",\n" +
                        "  \"legalName\": \"Heidenreich-Doyle\",\n" +
                        "  \"address\": \"1854 Mayfield Parkway\",\n" +
                        "  \"city\": \"Baochang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1996,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 43,\n" +
                        "  \"bpn\": \"Mydeo\",\n" +
                        "  \"legalName\": \"Ferry-Ferry\",\n" +
                        "  \"address\": \"9 Badeau Parkway\",\n" +
                        "  \"city\": \"Bacolod\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Agavaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 44,\n" +
                        "  \"bpn\": \"Oyonder\",\n" +
                        "  \"legalName\": \"Dicki, Mohr and Okuneva\",\n" +
                        "  \"address\": \"0 Shelley Trail\",\n" +
                        "  \"city\": \"Voiron\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 1998,\n" +
                        "  \"rating\": \"Scrophulariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 45,\n" +
                        "  \"bpn\": \"Blognation\",\n" +
                        "  \"legalName\": \"Hermann-Kreiger\",\n" +
                        "  \"address\": \"0 Valley Edge Hill\",\n" +
                        "  \"city\": \"Jadranovo\",\n" +
                        "  \"country\": \"Croatia\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Polemoniaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 46,\n" +
                        "  \"bpn\": \"Divanoodle\",\n" +
                        "  \"legalName\": \"Towne, Grant and Sipes\",\n" +
                        "  \"address\": \"8 Clove Plaza\",\n" +
                        "  \"city\": \"Calibutbut\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 1995,\n" +
                        "  \"rating\": \"Cistaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 47,\n" +
                        "  \"bpn\": \"Meevee\",\n" +
                        "  \"legalName\": \"Torp Group\",\n" +
                        "  \"address\": \"970 Doe Crossing Way\",\n" +
                        "  \"city\": \"Oakland\",\n" +
                        "  \"country\": \"United States\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Polemoniaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 48,\n" +
                        "  \"bpn\": \"Npath\",\n" +
                        "  \"legalName\": \"MacGyver and Sons\",\n" +
                        "  \"address\": \"85 Homewood Place\",\n" +
                        "  \"city\": \"Sapele\",\n" +
                        "  \"country\": \"Nigeria\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Convolvulaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 49,\n" +
                        "  \"bpn\": \"Voolia\",\n" +
                        "  \"legalName\": \"Wyman-Blick\",\n" +
                        "  \"address\": \"9 John Wall Hill\",\n" +
                        "  \"city\": \"Pelasgía\",\n" +
                        "  \"country\": \"Greece\",\n" +
                        "  \"score\": 1999,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 50,\n" +
                        "  \"bpn\": \"Photobug\",\n" +
                        "  \"legalName\": \"Hills Inc\",\n" +
                        "  \"address\": \"8753 Westerfield Hill\",\n" +
                        "  \"city\": \"Val-d'Or\",\n" +
                        "  \"country\": \"Canada\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 51,\n" +
                        "  \"bpn\": \"Kayveo\",\n" +
                        "  \"legalName\": \"Blanda-Cummerata\",\n" +
                        "  \"address\": \"1 8th Road\",\n" +
                        "  \"city\": \"La Ferté-Bernard\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Lentibulariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 52,\n" +
                        "  \"bpn\": \"Meevee\",\n" +
                        "  \"legalName\": \"Pagac-Koepp\",\n" +
                        "  \"address\": \"078 Eliot Crossing\",\n" +
                        "  \"city\": \"Corujeira\",\n" +
                        "  \"country\": \"Portugal\",\n" +
                        "  \"score\": 1987,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 53,\n" +
                        "  \"bpn\": \"Jaxworks\",\n" +
                        "  \"legalName\": \"Vandervort and Sons\",\n" +
                        "  \"address\": \"63766 Autumn Leaf Lane\",\n" +
                        "  \"city\": \"Limoeiro\",\n" +
                        "  \"country\": \"Brazil\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Verrucariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 54,\n" +
                        "  \"bpn\": \"Thoughtsphere\",\n" +
                        "  \"legalName\": \"Towne, Bogisich and Runolfsson\",\n" +
                        "  \"address\": \"685 Almo Parkway\",\n" +
                        "  \"city\": \"Manika\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2007,\n" +
                        "  \"rating\": \"Boraginaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 55,\n" +
                        "  \"bpn\": \"Innotype\",\n" +
                        "  \"legalName\": \"Kunze LLC\",\n" +
                        "  \"address\": \"32 Waxwing Point\",\n" +
                        "  \"city\": \"Zavoronezhskoye\",\n" +
                        "  \"country\": \"Russia\",\n" +
                        "  \"score\": 2010,\n" +
                        "  \"rating\": \"Onagraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 56,\n" +
                        "  \"bpn\": \"Wordpedia\",\n" +
                        "  \"legalName\": \"Douglas, Green and Jast\",\n" +
                        "  \"address\": \"8 Burning Wood Terrace\",\n" +
                        "  \"city\": \"Xinfeng\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1995,\n" +
                        "  \"rating\": \"Cactaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 57,\n" +
                        "  \"bpn\": \"Riffwire\",\n" +
                        "  \"legalName\": \"Senger Inc\",\n" +
                        "  \"address\": \"0971 Kensington Way\",\n" +
                        "  \"city\": \"Tall Ḩamīs\",\n" +
                        "  \"country\": \"Syria\",\n" +
                        "  \"score\": 2009,\n" +
                        "  \"rating\": \"Neckeraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 58,\n" +
                        "  \"bpn\": \"Rhyzio\",\n" +
                        "  \"legalName\": \"Hermann Group\",\n" +
                        "  \"address\": \"251 Cody Drive\",\n" +
                        "  \"city\": \"Rafaela\",\n" +
                        "  \"country\": \"Argentina\",\n" +
                        "  \"score\": 2013,\n" +
                        "  \"rating\": \"Cyperaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 59,\n" +
                        "  \"bpn\": \"Tekfly\",\n" +
                        "  \"legalName\": \"Klein-Corkery\",\n" +
                        "  \"address\": \"84478 Summerview Terrace\",\n" +
                        "  \"city\": \"Pataias\",\n" +
                        "  \"country\": \"Portugal\",\n" +
                        "  \"score\": 1998,\n" +
                        "  \"rating\": \"Brassicaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 60,\n" +
                        "  \"bpn\": \"Gabspot\",\n" +
                        "  \"legalName\": \"Schmeler and Sons\",\n" +
                        "  \"address\": \"76 Esch Circle\",\n" +
                        "  \"city\": \"Xiaoxiang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Cyperaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 61,\n" +
                        "  \"bpn\": \"Janyx\",\n" +
                        "  \"legalName\": \"Bergstrom LLC\",\n" +
                        "  \"address\": \"742 Troy Way\",\n" +
                        "  \"city\": \"Shengli\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2006,\n" +
                        "  \"rating\": \"Rubiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 62,\n" +
                        "  \"bpn\": \"Leenti\",\n" +
                        "  \"legalName\": \"Maggio-Ullrich\",\n" +
                        "  \"address\": \"051 Kropf Center\",\n" +
                        "  \"city\": \"Yuexi\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1997,\n" +
                        "  \"rating\": \"Poaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 63,\n" +
                        "  \"bpn\": \"Digitube\",\n" +
                        "  \"legalName\": \"Mosciski-Nitzsche\",\n" +
                        "  \"address\": \"681 Cambridge Junction\",\n" +
                        "  \"city\": \"Tengjia\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2012,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 64,\n" +
                        "  \"bpn\": \"Rhybox\",\n" +
                        "  \"legalName\": \"Hettinger-Tromp\",\n" +
                        "  \"address\": \"36 Pawling Parkway\",\n" +
                        "  \"city\": \"Pingshan\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2002,\n" +
                        "  \"rating\": \"Aquifoliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 65,\n" +
                        "  \"bpn\": \"Yabox\",\n" +
                        "  \"legalName\": \"Franecki, Labadie and O'Keefe\",\n" +
                        "  \"address\": \"59 Shoshone Terrace\",\n" +
                        "  \"city\": \"Guayabo Dulce\",\n" +
                        "  \"country\": \"Dominican Republic\",\n" +
                        "  \"score\": 2000,\n" +
                        "  \"rating\": \"Leucodontaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 66,\n" +
                        "  \"bpn\": \"Eimbee\",\n" +
                        "  \"legalName\": \"Lakin, Feeney and Walsh\",\n" +
                        "  \"address\": \"0017 Bonner Pass\",\n" +
                        "  \"city\": \"Capalayan\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 2000,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 67,\n" +
                        "  \"bpn\": \"Topicblab\",\n" +
                        "  \"legalName\": \"Pollich LLC\",\n" +
                        "  \"address\": \"0334 Eagle Crest Point\",\n" +
                        "  \"city\": \"Xiheshan\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1988,\n" +
                        "  \"rating\": \"Lamiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 68,\n" +
                        "  \"bpn\": \"Devpoint\",\n" +
                        "  \"legalName\": \"Heathcote, Robel and Heathcote\",\n" +
                        "  \"address\": \"3 Hanson Place\",\n" +
                        "  \"city\": \"Mūdīyah\",\n" +
                        "  \"country\": \"Yemen\",\n" +
                        "  \"score\": 1991,\n" +
                        "  \"rating\": \"Liliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 69,\n" +
                        "  \"bpn\": \"Wordpedia\",\n" +
                        "  \"legalName\": \"Lueilwitz Inc\",\n" +
                        "  \"address\": \"3 Mifflin Avenue\",\n" +
                        "  \"city\": \"Toulouse\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 2001,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 70,\n" +
                        "  \"bpn\": \"Jaxnation\",\n" +
                        "  \"legalName\": \"Price, Grimes and Hessel\",\n" +
                        "  \"address\": \"83581 Ridgeview Park\",\n" +
                        "  \"city\": \"Cajabamba\",\n" +
                        "  \"country\": \"Peru\",\n" +
                        "  \"score\": 1989,\n" +
                        "  \"rating\": \"Rosaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 71,\n" +
                        "  \"bpn\": \"Twimm\",\n" +
                        "  \"legalName\": \"Dietrich-Moore\",\n" +
                        "  \"address\": \"597 Leroy Drive\",\n" +
                        "  \"city\": \"Bondy\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 2010,\n" +
                        "  \"rating\": \"Loasaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 72,\n" +
                        "  \"bpn\": \"Latz\",\n" +
                        "  \"legalName\": \"Rogahn Inc\",\n" +
                        "  \"address\": \"9 Schiller Lane\",\n" +
                        "  \"city\": \"Odesskoye\",\n" +
                        "  \"country\": \"Russia\",\n" +
                        "  \"score\": 2005,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 73,\n" +
                        "  \"bpn\": \"Blogtags\",\n" +
                        "  \"legalName\": \"Pfeffer, Cummerata and Schuppe\",\n" +
                        "  \"address\": \"716 Riverside Drive\",\n" +
                        "  \"city\": \"Lidu\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2001,\n" +
                        "  \"rating\": \"Psilotaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 74,\n" +
                        "  \"bpn\": \"Topicware\",\n" +
                        "  \"legalName\": \"Brekke Group\",\n" +
                        "  \"address\": \"37181 Sloan Lane\",\n" +
                        "  \"city\": \"Tipolo\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 1998,\n" +
                        "  \"rating\": \"Asclepiadaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 75,\n" +
                        "  \"bpn\": \"Jaloo\",\n" +
                        "  \"legalName\": \"White-Gislason\",\n" +
                        "  \"address\": \"718 Lyons Alley\",\n" +
                        "  \"city\": \"Saint Helier\",\n" +
                        "  \"country\": \"Jersey\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Cactaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 76,\n" +
                        "  \"bpn\": \"Rhybox\",\n" +
                        "  \"legalName\": \"Bartell and Sons\",\n" +
                        "  \"address\": \"5930 Becker Pass\",\n" +
                        "  \"city\": \"Catriló\",\n" +
                        "  \"country\": \"Argentina\",\n" +
                        "  \"score\": 2009,\n" +
                        "  \"rating\": \"Pteridaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 77,\n" +
                        "  \"bpn\": \"Mydo\",\n" +
                        "  \"legalName\": \"Brown-Gerhold\",\n" +
                        "  \"address\": \"6 Lakewood Park\",\n" +
                        "  \"city\": \"Heqing\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2001,\n" +
                        "  \"rating\": \"Asclepiadaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 78,\n" +
                        "  \"bpn\": \"Browsedrive\",\n" +
                        "  \"legalName\": \"Klein, Smitham and Kshlerin\",\n" +
                        "  \"address\": \"911 Roxbury Circle\",\n" +
                        "  \"city\": \"Dziewin\",\n" +
                        "  \"country\": \"Poland\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Hydrophyllaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 79,\n" +
                        "  \"bpn\": \"Vidoo\",\n" +
                        "  \"legalName\": \"Conn-Sanford\",\n" +
                        "  \"address\": \"73 Scoville Court\",\n" +
                        "  \"city\": \"Santiago\",\n" +
                        "  \"country\": \"Chile\",\n" +
                        "  \"score\": 1979,\n" +
                        "  \"rating\": \"Ericaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 80,\n" +
                        "  \"bpn\": \"Topicshots\",\n" +
                        "  \"legalName\": \"Hintz Inc\",\n" +
                        "  \"address\": \"5 Cardinal Street\",\n" +
                        "  \"city\": \"Marseille\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 2008,\n" +
                        "  \"rating\": \"Apiaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 81,\n" +
                        "  \"bpn\": \"Jaxspan\",\n" +
                        "  \"legalName\": \"Wuckert-Walter\",\n" +
                        "  \"address\": \"38980 Tony Point\",\n" +
                        "  \"city\": \"Lanlacuni Bajo\",\n" +
                        "  \"country\": \"Peru\",\n" +
                        "  \"score\": 2004,\n" +
                        "  \"rating\": \"Myrtaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 82,\n" +
                        "  \"bpn\": \"Yakitri\",\n" +
                        "  \"legalName\": \"Runolfsson, Nicolas and Block\",\n" +
                        "  \"address\": \"4752 Corscot Terrace\",\n" +
                        "  \"city\": \"Yajiang\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1996,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 83,\n" +
                        "  \"bpn\": \"Devify\",\n" +
                        "  \"legalName\": \"Hodkiewicz and Sons\",\n" +
                        "  \"address\": \"29825 Superior Park\",\n" +
                        "  \"city\": \"Si Racha\",\n" +
                        "  \"country\": \"Thailand\",\n" +
                        "  \"score\": 1988,\n" +
                        "  \"rating\": \"Myrtaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 84,\n" +
                        "  \"bpn\": \"Thoughtstorm\",\n" +
                        "  \"legalName\": \"Gutkowski LLC\",\n" +
                        "  \"address\": \"8 Kennedy Lane\",\n" +
                        "  \"city\": \"Oefatu\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1992,\n" +
                        "  \"rating\": \"Pertusariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 85,\n" +
                        "  \"bpn\": \"Realmix\",\n" +
                        "  \"legalName\": \"Rosenbaum and Sons\",\n" +
                        "  \"address\": \"28969 Farragut Center\",\n" +
                        "  \"city\": \"Taoyuan\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 2011,\n" +
                        "  \"rating\": \"Trypetheliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 86,\n" +
                        "  \"bpn\": \"Yombu\",\n" +
                        "  \"legalName\": \"Rippin, Bruen and Beer\",\n" +
                        "  \"address\": \"14 Elka Court\",\n" +
                        "  \"city\": \"Zherdevka\",\n" +
                        "  \"country\": \"Russia\",\n" +
                        "  \"score\": 1998,\n" +
                        "  \"rating\": \"Apocynaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 87,\n" +
                        "  \"bpn\": \"Thoughtstorm\",\n" +
                        "  \"legalName\": \"Hegmann-Heaney\",\n" +
                        "  \"address\": \"904 Miller Road\",\n" +
                        "  \"city\": \"Ziyuan\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1990,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 88,\n" +
                        "  \"bpn\": \"Skimia\",\n" +
                        "  \"legalName\": \"Torphy, Boyle and Grant\",\n" +
                        "  \"address\": \"67 Prentice Plaza\",\n" +
                        "  \"city\": \"Nusajaya\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1993,\n" +
                        "  \"rating\": \"Convolvulaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 89,\n" +
                        "  \"bpn\": \"Avavee\",\n" +
                        "  \"legalName\": \"Lowe, Anderson and Skiles\",\n" +
                        "  \"address\": \"095 Bluestem Alley\",\n" +
                        "  \"city\": \"Vaiņode\",\n" +
                        "  \"country\": \"Latvia\",\n" +
                        "  \"score\": 1982,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 90,\n" +
                        "  \"bpn\": \"Tazz\",\n" +
                        "  \"legalName\": \"Bode-Hyatt\",\n" +
                        "  \"address\": \"60857 Hoard Court\",\n" +
                        "  \"city\": \"Banjar Banyualit\",\n" +
                        "  \"country\": \"Indonesia\",\n" +
                        "  \"score\": 1990,\n" +
                        "  \"rating\": \"Simaroubaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 91,\n" +
                        "  \"bpn\": \"Eire\",\n" +
                        "  \"legalName\": \"O'Kon Group\",\n" +
                        "  \"address\": \"0 Northfield Point\",\n" +
                        "  \"city\": \"Bāsht\",\n" +
                        "  \"country\": \"Iran\",\n" +
                        "  \"score\": 1984,\n" +
                        "  \"rating\": \"Tiliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 92,\n" +
                        "  \"bpn\": \"Buzzster\",\n" +
                        "  \"legalName\": \"Carroll-Tromp\",\n" +
                        "  \"address\": \"027 Nelson Crossing\",\n" +
                        "  \"city\": \"El Realejo\",\n" +
                        "  \"country\": \"Nicaragua\",\n" +
                        "  \"score\": 1994,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 93,\n" +
                        "  \"bpn\": \"Zoomlounge\",\n" +
                        "  \"legalName\": \"Abbott-Kuhn\",\n" +
                        "  \"address\": \"9656 Grayhawk Lane\",\n" +
                        "  \"city\": \"Saint-Augustin\",\n" +
                        "  \"country\": \"Canada\",\n" +
                        "  \"score\": 1993,\n" +
                        "  \"rating\": \"Liliaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 94,\n" +
                        "  \"bpn\": \"Voonder\",\n" +
                        "  \"legalName\": \"Bogisich Group\",\n" +
                        "  \"address\": \"356 Canary Trail\",\n" +
                        "  \"city\": \"Metapán\",\n" +
                        "  \"country\": \"El Salvador\",\n" +
                        "  \"score\": 1989,\n" +
                        "  \"rating\": \"Asteraceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 95,\n" +
                        "  \"bpn\": \"Dynabox\",\n" +
                        "  \"legalName\": \"Donnelly, Dibbert and Pouros\",\n" +
                        "  \"address\": \"12 Esker Trail\",\n" +
                        "  \"city\": \"Kotido\",\n" +
                        "  \"country\": \"Uganda\",\n" +
                        "  \"score\": 1995,\n" +
                        "  \"rating\": \"Scrophulariaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 96,\n" +
                        "  \"bpn\": \"Lajo\",\n" +
                        "  \"legalName\": \"Bailey, Braun and Rath\",\n" +
                        "  \"address\": \"2 Manitowish Place\",\n" +
                        "  \"city\": \"Castelnaudary\",\n" +
                        "  \"country\": \"France\",\n" +
                        "  \"score\": 1986,\n" +
                        "  \"rating\": \"Fabaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 97,\n" +
                        "  \"bpn\": \"Cogidoo\",\n" +
                        "  \"legalName\": \"Dickens and Sons\",\n" +
                        "  \"address\": \"17 Montana Crossing\",\n" +
                        "  \"city\": \"Divisoria\",\n" +
                        "  \"country\": \"Philippines\",\n" +
                        "  \"score\": 1985,\n" +
                        "  \"rating\": \"Caryophyllaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 98,\n" +
                        "  \"bpn\": \"Meedoo\",\n" +
                        "  \"legalName\": \"Labadie-McDermott\",\n" +
                        "  \"address\": \"7324 Meadow Vale Drive\",\n" +
                        "  \"city\": \"Pampa del Infierno\",\n" +
                        "  \"country\": \"Argentina\",\n" +
                        "  \"score\": 1988,\n" +
                        "  \"rating\": \"Malvaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 99,\n" +
                        "  \"bpn\": \"Jayo\",\n" +
                        "  \"legalName\": \"Feeney Inc\",\n" +
                        "  \"address\": \"4862 Kedzie Hill\",\n" +
                        "  \"city\": \"Novi Bečej\",\n" +
                        "  \"country\": \"Serbia\",\n" +
                        "  \"score\": 1996,\n" +
                        "  \"rating\": \"Ceratophyllaceae\"\n" +
                        "}, {\n" +
                        "  \"id\": 100,\n" +
                        "  \"bpn\": \"Avavee\",\n" +
                        "  \"legalName\": \"Sawayn-Schuster\",\n" +
                        "  \"address\": \"54077 Dryden Junction\",\n" +
                        "  \"city\": \"Sidu\",\n" +
                        "  \"country\": \"China\",\n" +
                        "  \"score\": 1992,\n" +
                        "  \"rating\": \"Bryaceae\"\n" +
                        "}]\n"
                ,new TypeReference<List<DashBoardDto>>() {});
//        DashBoardDto dashBoardDto;
//        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/config/fake-data/dashboard.csv"));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            String[] field = line.split(";");
//            dashBoardDto = new DashBoardDto();
//            dashBoardDto.setId(Long.valueOf(field[0]));
//            dashBoardDto.setBpn(field[1]);
//            dashBoardDto.setLegalName(field[2]);
//            dashBoardDto.setAddress(field[3]);
//            dashBoardDto.setCountry(field[4]);
//            dashBoardDto.setScore(Float.valueOf(field[5]));
//            dashBoardDto.setRating(field[6]);
//            dashBoardDtos.add(dashBoardDto);
//        }
        return ResponseEntity.ok().body(dashBoardDtos);
    }


    @Scheduled(cron = "0 * * * * *")
    public ResponseEntity<List<DashBoardDto>> makeSchedule() throws IOException {
        log.info("REST request to make a schedule {} ",host);
        var headers = new HttpHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> rest = restTemplate.exchange(host, HttpMethod.GET,request,Object.class);
        log.info(rest.getBody().toString());
        return ResponseEntity.ok().body(new ArrayList<>());
    }

}

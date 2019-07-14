# AnonymousChat on P2P Networks

Lo scopo del progetto è quello di creare una rete Peer to Peer in cui viene creato un sistema di chat anonime. In tale sistema i Peers riescono a inviare messaggi, in modo anonimo, in una determinata stanza a cui accedono. In ogni stanza e' possibile creare una challenge in cui un peer sceglie un numero da 1 a 10. A tutti gli altri peers che fanno parte di quella determinata stanza viene inviato un messaggio per avvertirli che in quella stanza è stata creata una challenge da un peer(quest'ultimo rimane comunque in anonimato) e devono riuscire ad indovinare il numero che il peer ha scelto.  

<h2>Funzionalità:</h2>
<ul>
  <li>Creare una stanza</li>
  <li>Collegarsi a una stanza</li>
  <li>Inviare un messaggio alla stanza</li>
  <li>Lasciare la stanza</li>
  <li>Lasciare la rete (funzionalità aggiuntiva)</li>
  <li>Creare una challenge (funzionalità aggiuntiva)</li>
  <li>Partecipare alla challenge (funzionalità aggiuntiva)</li>
 </ul>

<h2>Protocollo di base:</h2> 
Data una rete p2p composta da n nodi l’obiettivo è inviare messaggi in anonimato. 
Supponiamo di avere un peer "A" che crea la stanza "home" e fa join per questa stanza. Tutti gli altri peer possono fare join alla stanza "home". Nel momento in cui un peer invia un messaggio nella stanza "home", tutti i peers della stanza riceveranno tale messaggio. 

<h2>Protocollo della challenge:</h2> 
Data una rete p2p composta da n nodi l’obiettivo è riuscire a partecipare ad una challenge creata da un peer all'interno di tale stanza.. 
Supponiamo di avere un peer "A" che crea la stanza "home" e fa join per questa stanza. Tutti gli altri peer possono fare join alla stanza "home". Supponiamo che un peer "B" effettui la join alla stanza "home". A questo punto B può creare una challlenge(diventando leader della challenge) nella stanza e scegliere il numero vincente. Tutti gli altri peers possono scegliere di fare join della challenge creata e indovinare il numero. Se un peer indovina il numero verrà inviato a tutti gli altri peers che parteciapno alla challenge che un peer ha indovinato il numero, altrimenti vengono informati che un peer non ha indovinato il numero. Ogni peer che crea la challenge diventa leader di quella challenge e quindi nessuno può creare altre challenge in quella stanza perche' può esserci uno solo leader per la challenge.  

<br>



<h2>Requisiti necessari:</h2>
<b>Per la compilazione</b> è necessario avere almeno una versione di java 1.8.

<br><b>Per i test</b> è necessario usare almeno 2 peer. 

<h2>Testing svolto:</h2>
L'obiettivo principale del testing condotto era capire se effettivamente tutti i peer collegati a una stanza ricevevano i messaggi e testare se la logica della challenge risulti completamente funzionante
<br>La <b>strategia adottata per il testing è la seguente</b>:
<br><br> Sono stati creati 4 peers. Tali peers vengono utilizzati per chiamare i vari metodi dell implementazione. Ad esempio : <br><ul><li>PeerA crea una stanza e si verifica se il valore di ritorno e' true. Poi si ripete la stessa operazione con lo stesso peer verificando che il risultato si false, perche' risulta che la stanza e' gia' stata creata.</li>
<li>I peers fanno join della stanza, ma un peer non puo' fare join della stessa stanza</li>
<li> Un peer lascia una stanza e non puo' fare leave della stessa stanza se l'ha gia' lasciata</li>
<li>Un peer di una stanza puo' inviare un messaggio a tutti i peers che sono nella stanza, se ha fatto leaveRoom allora non puo' inviare un messaggio in quella stanza. Inoltre se la stanza non esiste un peer non puo' inviare il messaggio</li>
  <li>Un peer puo' lasciare la rete</li>
  <li>Un Peer puo' creare una challenge in una stanza se ha fatto precedentemente join altrimenti l'operazione non andra' a buon fine</li>
  <li>Un peer può fare join alla challenge se ha fatto precedentemente join alla stanza in cui e' stata creata la challenge altrimenti l'operazione non andra' a buon fine. Facendo join propone il valore per vincere il gioco</li>
  <li> Viene verificato se un peer ha indivinato il numero proposto dal peer leader o meno. Viene inviato un messaggio in cui si notifica un vincitore o un perdente a tutti i peers iscritti alla challenge.</li>
  <li> Si invia un messaggio per le challenge ai peers solo se si è iscritti alla challenge, altrimenti l'operazione non andrà a buon fine</li>

</ul>


<h2>Docker:</h2>

<ul>
<li><b>Build Docker container</b>
docker build --no-cache -f "Dockerfile" -t anonymouschat .
</li>

<li>
<b>Master Peer</b>
docker run -i --name MASTER-PEER -e MASTERIP="127.0.0.1" -e ID=0 anonymouschat
</li>

<li><b>Peer generico</b>
In seguito all avvio del master si possono avviare gli altri peers:

docker run -i --name PEER-1 -e MASTERIP="172.17.0.2" -e ID=1 anonymouschat
</li>

<h2>Sviluppato da:</h2>
<b>Pipino Claudia - 0522500501</b>

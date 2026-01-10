# Dama Italiana

Questo repository contiene l'implementazione di un agente intelligente progettato per giocare a Dama rispettando le regole ufficiali.

## 🚀 Caratteristiche principali
* **Regolamento Completo**: Supporto all'obbligo di cattura, priorità di presa e gestione ricorsiva delle catture multiple (trattate come mosse atomiche)
* **Motore di Gioco**: Algoritmo Minimax ottimizzato per la ricerca nello spazio degli stati.
* **Ottimizzazioni Avanzate**: 
    * **Potatura Alpha-Beta**: Riduce lo spazio di ricerca ignorando i rami non influenti.
    * **Move Ordering**: Ordinamento euristico delle mosse per velocizzare i tagli dell'algoritmo.
* **Interfaccia Grafica**: Sviluppata in Java con JavaFX.

## 🧠 Intelligenza Artificiale
L'agente utilizza una funzione di valutazione euristica che analizza:
* **Materiale**: Valore dinamico per pedine e dame, con incremento del punteggio nelle fasi di *End Game*.
* **Posizionamento**: Bonus per il controllo del centro, dei bordi e della riga di fondo per la difesa.
* **Strategia**: Incentivi per la coesione dei pezzi ("formazione a testuggine") e mobilità differenziale.

## 📊 Performance
L'agente è progettato per rispondere entro un massimo di **3 secondi per mossa**. Dalle analisi sperimentali:
* A **profondità 6**, la potatura Alpha-Beta riduce il carico computazionale di circa il 60%, mentre l'ordinamento ha un effetto marginale.
* A **profondità 10**, l'ordinamento mostra effetti tangibili sia sui nodi visitati che sul tempo di esecuzione.

## 🛠️ Tecnologie utilizzate
* **Linguaggio**: Java
* **GUI**: JavaFX
* **Algoritmi**: Minimax, Potatura Alpha-Beta

---
**Autore**: Antonio Galdo (Università degli Studi di Salerno)

# szgarch-ulti

## API

#### Kliens - Szerver
- Bejelentkezés: `{"type":"login", "name":<string>, "password":<string>}` :ok:
- Kijelentkezés: `{"type":"logout"}` :ok:
- Regisztráció: `{"type":"register", "email:<string>" "name":<string>, "password":<string>}` :ok:
- Vendég bejelentkezés: `{"type":"guestlogin"}` :ok:
- Chat üzenet: `{"type":"chat", "message":<string>}` :ok:
- Chat szoba létrehozása: `{"type":"newchat", "name":<string>, "maxmembers":<int>}` :ok:
- Chat szoba váltása: `{"type":"tochat", "name":<string>}` :ok:
- Chat szoba elhagyása: `{"type":"leavechat"}` :ok:
- Összes chat szoba lekérése: `{"type":"getallchat"}` :ok:
- Ulti szoba létrehozása: `{"type":"newulti", "name":<string>, "maxmembers":<int>}` :ok:
- Ulti szoba váltása: `{"type":"toulti", "name":<string>}` :ok:
- Ulti szoba elhagyása: `{"type":"leaveulti"}` :ok:
- Összes ulti szoba lekérése: `{"type":"getallulti"}` :ok:
- Felhasználók listázása adminnak: `{"type":"listactiveplayers"}` :ok:
- Felhasználó kidobása adminnak: `{"type":"kick", "name":<string>}` :ok:
- Toplista lekérés: `{"type":"gettoplist"}` :ok:
- Játék bemondás: `{"type":"gameselection", "gameType":<int>, "card1":{"suit":<String>, "value":<String>}, "card2":{"suit":<String>, "value":<String>}}` :ok:
- Passzolás: `{"type":"pass"}` :ok:
- Lapfelvétel: `{"type":"pickupcards"}` :ok:
- Játék jóváhagyása: `{"type":"confirmgame"}` :ok:
- Lap lerakás: `{"type":"playcard", "card":{"suit":<String>, "value":<String>}}` :ok:

#### Szerver - Kliens
- Bejelentkezés válasz: `{"type":"login", "success":<boolean>, "name":<string>, "playerType":<string>}` :ok:
- Kijelentkezés válasz: `{"type":"logout"}` :ok:
- Regisztráció válasz: `{"type":"register", "success":<boolean>, "fault":<string>}` :ok:
- Chat üzenet: `{"type":"chat", "message":<string>, from:<string>}` :ok:
- Hiba üzenet: `{"type":"error", "message":<string>}` :ok:
- Chat szoba elkészülése: `{"type":"newchat", "success":<bool>}` :ok:
- Chat szoba váltása: `{"type":"tochat", "success":<bool>, "message":<string>}` :ok:
- Chat szoba nevek leküldése: `{"type":"allchat" "rooms":[{"name":<string>, "actual":<int>, "max":<int>}]}` :hankey:
- Ulti szoba elkészülése: `{"type":"newulti", "success":<bool>}` :ok:
- Ulti szoba váltása: `{"type":"toulti", "success":<bool>, "message":<string>}` :ok:
- Ulti szoba nevek leküldése: `{"type":"allulti", "playersinultiroom":[{"roomName":<String>, "names":[<String>]}]}` :ok:
- Felhasználónevek leküldése: `{"type":"activeplayerlist", "nameList":[<string>]}` :ok:
- Kidobás játákost: `{"type":"kickplayer"}` :ok:
- Kidobás válasz adminnak: `{"type":"kick", "success":<boolean>}` :ok:
- Toplista válasz: `{"type":"toplist", "toplist":{<string>: <int>}}` :ok:
- Ulti szobába 3. ember belépett: `{"type":"startulti"}` :ok:
- Osztás válasz: `{"type":"deal", "cards":[{"suit":<String>, "value":<String>}], "isstarter":<boolean>}` :ok:
- Következő játékos jön: `{"type":"playeronturn", "name":<string>, "isItMe":<bool>}` :ok:
- Játék bemondás válasz: `{"type":"gameselected", "name":<String>, "isItMe":<bool>, "gameType":<int>}` :ok:
- Felvett lapok válasz: `{"type":"pickedupcards", "card1":{"suit":<String>, "value":<String>}, "card2":{"suit":<String>, "value":<String>}}` :ok:
- Fel kell venni vagy le kell játszani válasz: `{"type":"hastoconfirm"}` :ok:
- Játék kezdődik: `{"type":"startgame"}` :ok:
- Lap elvitel: `{"type":"takecards", "name":<String>, "isItMe":<bool>, "cards":[{"suit":<String>, "value":<String>}]}`
- Eredmények mutatása: `{"type":"showresult", "points":[{<string>: <int>}]}`
- Lap lerakás válasz: `{"type":"playedcard", "name":<String>, "isItMe":<bool>, "card":{"suit":<String>, "value":<String>}}` :ok:
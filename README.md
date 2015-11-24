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
- Ulti szoba elhagyása: `{"type":"leaveulti"}`
- Összes ulti szoba lekérése: `{"type":"getallulti"}` :ok:
- Felhasználók listázása adminnak: `{"type":"listactiveplayers"}`
- Felhasználó kidobása adminnak: `{"type":"kick", "name":<string>}`
- Toplista lekérés: `{"type":"gettoplist"}`
- Játék bemondás: `{"type":"gameselection", "gametype":<int>, "card1":<string>, "card2":<string>}`
- Passzolás: `{"type":"pass"}`
- Lapfelvétel: `{"type":"pickupcards"}`
- Játék jóváhagyása: `{"type":"confirmgame"}`
- Lap lerakás: `{"type":"playcard", "card":<string>}`

#### Szerver - Kliens
- Bejelentkezés válasz: `{"type":"login", "success":<boolean>, "name":<string>, "playertype":<string>}` :ok:
- Kijelentkezés válasz: `{"type":"logout"}` :ok:
- Regisztráció válasz: `{"type":"register", "success":<boolean>, "fault":<string>}` :ok:
- Chat üzenet: `{"type":"chat", "message":<string>, from:<string>}` :ok:
- Hiba üzenet: `{"type":"error", "message":<string>}` :ok:
- Chat szoba elkészülése: `{"type":"newchat", "success":<bool>}` :ok:
- Chat szoba váltása: `{"type":"tochat", "success":<bool>, "message":<string>}` :ok:
- Chat szoba nevek leküldése: `{"type":"allchat" "rooms":[{"name":<string>, "actual":<int>, "max":<int>}]}` :ok:
- Ulti szoba elkészülése: `{"type":"newulti", "success":<bool>}` :ok:
- Ulti szoba váltása: `{"type":"toulti", "success":<bool>, "message":<string>}`
- Ulti szoba nevek leküldése: `{"type":"allulti", "playersinultiroom":[{"roomName"=<String>, "names"=[<String>]}]}` :ok:
- Felhasználónevek leküldése: `{"type":"activeplayerlist", "namelist":[<string>]}`
- Kidobás játákost: `{"type":"kickplayer"}`
- Kidobás válasz adminnak: `{"type":"kick", "success":<boolean>}`
- Toplista válasz: `{"type":"toplist", "toplist":[<string, int>]}`
- Ulti szobába 3. ember belépett: `{"type":"startulti"}`
- Osztás válasz: `{"type":"deal", "cards":[<string>], "isstarter":<boolean>}`
- Következő játékos jön: `{"type":"playeronturn", "name":<string>}`
- Felvett lapok válasz: `{"type":"pickedupcards", "card1":<string>, "card2":<string>}`
- Fel kell venni vagy le kell játszani válasz: `{"type":"hastoconfirm"}`
- Játék kezdődik: `{"type":"startgame"}`
- Lap elvitel: `{"type":"takecards", "cards":[<string>]}`
- Eredmények mutatása: `{"type":"showresult", "points":[<string, int>]}`
# szgarch-ulti

## API

#### Kliens - Szerver
- Bejelentkezés: `{"type":"login", "name":<string>, "password":<string>}`
- Kijelentkezés: `{"type":"logout"}`
- Regisztráció: `{"type":"register", "email:<string>" "name":<string>, "password":<string>}`
- Vendég bejelentkezés: `{"type":"guestlogin"}`
- Chat üzenet: `{"type":"chat", "message":<string>}`
- Chat szoba létrehozása: `{"type":"newchat", "name":<string>, "maxmembers":<int>}`
- Chat szoba váltása: `{"type":"tochat", "name":<string>}`
- Chat szoba elhagyása: `{"type":"leavechat"}`
- Összes chat szoba lekérése: `{"type":"getallchat"}`

#### Szerver - Kliens
- Bejelentkezés válasz: `{"type":"login", "success":<boolean>}`
- Kijelentkezés válasz: `{"type":"logout", "success":<boolean>}`
- Regisztráció válasz: `{"type":"register", "success":<boolean>, "fault":<string>}`
- Chat üzenet: `{"type":"chat", "message":<string>, from:<string>}`
- Hiba üzenet: `{"type":"error", "message":<string>}`
- Chat szoba elkészülése: `{"type":"newchat", "succes":<bool>}`
- Chat szoba váltása: `{"type":"tochat", "name":<string>}`
- Chat szoba nevek leküldése: `{"type":"allchat" "names":[<string>]}`
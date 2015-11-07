# szgarch-ulti

## API

#### Kliens - Szerver
- Bejelentkezés: {"type":"login", "name":<string>, "password":<string>}
- Regisztráció: {"type":"register", "email:<string>" "name":<string>, "password":<string>}
- Vendég bejelentkezés: {"type":"guestlogin"}
- Chat üzenet: {"type":"chat", "message":<string>}

#### Szerver - Kliens
- Bejelentkezés válasz: {"type":"login", "success":<boolean>}
- Regisztráció válasz: {"type":"register", "success":<boolean>, "fault":<string>}
- Chat üzenet: {"type":"chat", "message":<string>, from:<string>}
- Hiba üzenet: {"type":"error", "message":<string>}
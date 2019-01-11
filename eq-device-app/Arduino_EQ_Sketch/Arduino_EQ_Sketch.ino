#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "electric-quizer.firebaseio.com"
#define FIREBASE_AUTH "get_your_auth"
#define WIFI_SSID "iMäks"
#define WIFI_PASSWORD "get_your_pass"
#define DEVICE_NAME "NodeMCU"

#define KEEP_ALIVE D0
#define PLAYER_1 D1
#define PLAYER_2 D4
#define PLAYER_3 D5
#define PLAYER_4 D7

class Device {
  private:
    boolean available = true;
    String mac;
    String name;
  public:
    Device(String mac, String name) {
      this->mac = mac;
      this->name = name;
    }
    String get_name() {
      return (name);
    }
    String get_mac() {
      return (mac);
    }
    boolean is_available() {
      return (available);
    }
};

void(* resetFunc) (void) = 0; //declare reset function @ address 0

int watchdog_timeout = 1; // One minute  timeout for WiFi watchdog

static void wifiWatchdog(void) {
  static uint32_t ms = millis();
  static uint8_t watchdog = 0;

  if (millis() - ms > 1000 * 6) {
    ms = millis();
    if (WiFi.status() != WL_CONNECTED) {
      if (++watchdog < watchdog_timeout * 10) { // timeout in minutes ( * 10 )
        if (watchdog == 1) {
          Serial.println("WIFI: arming network watchdog (reboot in " + String(watchdog_timeout) + " min.)");
        }
      } else {
        Serial.println("WIFI: still not connected, triggering reboot ...");

        ESP.restart();
      }
    } else {
      if (watchdog) {
        Serial.println("WIFI: network is back, disarming watchdog");
        watchdog = 0;
      }
    }
  }
}

void setup() {
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, HIGH);

  pinMode(KEEP_ALIVE, OUTPUT);
  pinMode(PLAYER_1, OUTPUT);
  pinMode(PLAYER_2, OUTPUT);
  pinMode(PLAYER_3, OUTPUT);
  pinMode(PLAYER_4, OUTPUT);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);

    wifiWatchdog();
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST);

  delay(200);
}

bool deviceInit = false;
bool alreadyInBase = false;
String deviceRef = "";
bool loadedDevices = false;

String gameRef = "";
bool streamSet = false;
int lastKeepAlive = 0;

int questionTimeInMs = 0;
int questionCount = 0;
int questionNr = 0;

int startTime = 0;
int currentTime = 0;
bool gameRunning = false;

void loop() {
  delay(100);

  wifiWatchdog();
  if (Firebase.failed()) {
    Serial.print("stream broken --> restarting");
    delay(200);
    resetFunc();
  }

  if (!deviceInit) {
    digitalWrite(LED_BUILTIN, HIGH);
    Device device = Device(WiFi.macAddress(), DEVICE_NAME);
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& deviceJSON = jsonBuffer.createObject();
    deviceJSON["available"] = (device.is_available()) ? true : false;
    deviceJSON["name"] = device.get_name();
    deviceJSON["mac"] = device.get_mac();
    FirebaseObject child = Firebase.get("devices");
    if (child.success()) {
      Serial.println("get devices check");
      Serial.flush();
      JsonObject& obj = child.getJsonVariant();
      for (auto kv : obj) {
        loadedDevices = true;
        JsonObject& value = kv.value;
        if (value.get<String>("mac").equals(device.get_mac())) {
          alreadyInBase = true;
          deviceRef = String(kv.key);
        }
      }
      if (!alreadyInBase && loadedDevices) {
        deviceRef = Firebase.push("devices", deviceJSON);
        Serial.print("pushed: /device: ");
        Serial.print(deviceRef + " ");
        Serial.println(device.get_name());
        deviceInit = true;
      } else {
        if (loadedDevices) {
          String devicePath = "devices/" + deviceRef + "/available";
          Serial.print("need to update: ");
          Serial.println(devicePath);
          Firebase.setBool(devicePath, true);
          deviceInit = true;
        } else {
          Serial.println("failed loading");
        }
      }
    } else {
      Serial.println("get devices failed");
    }
  } else {
    digitalWrite(LED_BUILTIN, LOW);
  }

  if (gameRef.length() < 1) {
    FirebaseObject foGames = Firebase.get("games");
    if (foGames.success()) {
      Serial.println("searching for game");
      JsonObject& games = foGames.getJsonVariant();
      for (auto kv : games) {
        JsonObject& value = kv.value;
        if (value.get<String>("deviceId").equals(DEVICE_NAME)) {
          gameRef = "games/" + String(kv.key);
          questionTimeInMs = value.get<int>("questionTime") * 1000;
          questionCount = value.get<int>("questionCount");
          questionNr = value.get<int>("questionNr");
          Serial.print("Node is linked to: ");
          Serial.println(value.get<String>("name"));
        }
      }
      delay(2000);
    } else {
      Serial.println("get games failed");
      Serial.flush();
    }

  } else {
    // activate stream to current game
    if (!streamSet) {
      Serial.println(gameRef);
      Serial.flush();
      Firebase.stream(gameRef);
      streamSet = true;
    } else {
      if (Firebase.available()) {
        FirebaseObject event = Firebase.readEvent();
        String eventType = event.getString("type");
        eventType.toLowerCase();
        if (eventType == "put") {
          FirebaseObject currentGame = Firebase.get(gameRef);
          Serial.println("Ready to (schock)");
          boolean player1 = currentGame.getBool("feed1");
          boolean player2 = currentGame.getBool("feed2");
          boolean player3 = currentGame.getBool("feed3");
          boolean player4 = currentGame.getBool("feed4");
          gameRunning = currentGame.getBool("running");

          if (player1) {
            analogWrite(PLAYER_1, 255);
            Serial.println("Give player1 feedback");
            Firebase.setBool(gameRef + "/feed1", false);
          } else {
            analogWrite(PLAYER_1, 0);
          }
          if (player2) {
            analogWrite(PLAYER_2, 255);
            Serial.println("Give player2 feedback");
            Firebase.setBool(gameRef + "/feed2", false);
          } else {
            analogWrite(PLAYER_2, 0);

          }
          if (player3) {
            analogWrite(PLAYER_3, 255);
            Serial.println("Give player3 feedback");
            Firebase.setBool(gameRef + "/feed3", false);
          } else {
            analogWrite(PLAYER_3, 0);

          }
          if (player4) {
            analogWrite(PLAYER_4, 255);
            Serial.println("Give player4 feedback");
            Firebase.setBool(gameRef + "/feed4", false);
          } else {
            analogWrite(PLAYER_4, 0);
          }
        }

        //keep alive to maintain stream conncetion
        int ms = millis();
        if (ms - lastKeepAlive > 30000) {
          digitalWrite(KEEP_ALIVE, HIGH);
          Firebase.setInt(gameRef + "/keepAlive", ms);
          lastKeepAlive = ms;
          Serial.println("keep alive"); //hack to maintain the stream connction
          digitalWrite(KEEP_ALIVE, LOW);
        }
      }


      if (gameRunning) {
        if (startTime == 0) {
          startTime = millis();
        }

        if (questionNr < questionCount) {
          currentTime = millis();
          if (currentTime - startTime >= questionTimeInMs) {
            startTime = currentTime;
            Firebase.setInt(gameRef + "/questionNr", questionNr++);
          }
        } else {
          Serial.println("Game finished!");
          delay(5000);
        }
      } else {
        Serial.println("Waiting for game to start...");
        delay(2000);
      }

    }
  }
}

# Carnet de notes

Application Android native de prise de notes personnelles avec organisation
par categorie, developpee en Kotlin selon l'architecture MVP.

---

## Description

Carnet de notes permet a l'utilisateur de creer, modifier, supprimer et
organiser ses notes personnelles. Chaque note possede un titre, un contenu
textuel et une categorie representee par un indicateur de couleur.

---

## Fonctionnalites

- Creer une note avec titre, contenu et categorie
- Modifier une note existante
- Supprimer une note avec confirmation (appui long ou swipe)
- Afficher la date de creation et de derniere modification
- Rechercher dans les notes par mots-cles (titre et contenu)
- Trier les notes par date de creation, date de modification ou titre
- Categoriser les notes avec un indicateur visuel (couleur)

---

## Architecture

Le projet suit l'architecture MVP :
com.example.Myguelprojetnativesolo/
├── constants/        → NoteConstants.kt

├── contract/         → NoteContract.kt

├── model/            → Note.kt, Category.kt, NoteRepository.kt

├── presenter/        → NotePresenter.kt

└── view/             → MainActivity.kt, NoteListActivity.kt
NoteEditorActivity.kt, NoteAdapter.kt
---

## Categories disponibles

| Categorie | Couleur       |
|-----------|---------------|
| GENERAL   | Gris #9E9E9E  |
| WORK      | Bleu #2196F3  |
| IDEA      | Jaune #FFC107 |
| IMPORTANT | Rouge #F44336 |

---

## Prerequis

- Android Studio Hedgehog (2023.1.1) ou plus recent
- JDK 17 ou plus recent
- Android SDK API 24 (Android 7.0) minimum
- Gradle 8.0 ou plus recent
- Un emulateur Android ou un appareil physique (API 24+)

---

## Installation et execution

### 1. Cloner le projet

```bash
git clone https://github.com/ton-username/ProjetNativeSolo.git
cd ProjetNativeSolo
```

### 2. Ouvrir dans Android Studio

1. Lancer Android Studio
2. Cliquer sur File → Open
3. Selectionner le dossier ProjetNativeSolo
4. Attendre la synchronisation Gradle

### 3. Configurer l'emulateur

1. Aller dans Tools → Device Manager
2. Cliquer Create Device
3. Choisir un appareil (ex : Pixel 6)
4. Choisir une image systeme API 34
5. Cliquer Finish

### 4. Lancer l'application

1. Selectionner l'emulateur dans la barre du haut
2. Cliquer sur Run app
3. L'application se lance sur l'emulateur

---

## Executer les tests

### Tests unitaires

```bash
# Via Android Studio
Clic droit sur le dossier test/ → Run Tests in ...

# Via terminal
./gradlew test
```

### Tests bout-en-bout

```bash
# Via Android Studio (emulateur requis)
Clic droit sur le dossier androidTest/ → Run Tests in ...

# Via terminal
./gradlew connectedAndroidTest
```

---

## Dependances principales

```gradle
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

---

## Rapport d'utilisation de l'IA 
Outil utilisé : Claude (Anthropic)
Comment j'ai utilisé l'IA :

Je me suis appuyé sur ses suggestions pour écrire du code Kotlin (modèles, presenters, activités, adapter), que j’ai ensuite modifié et intégré moi-mêmeCorrection des erreurs de compilation
L’IA m’a aidé pour la :
    Génération de l'architecture MVP du projet
    Génération des tests unitaires et bout-en-bout
    Rédaction du README.md


Réflexion critique :

L’IA m’a surtout permis de gagner du temps en me donnant des pistes et des exemples de départ. Cependant, j’ai dû adapter, corriger et compléter plusieurs parties du code pour qu’elles correspondent réellement aux besoins de mon projet. Son utilisation m’a aussi aidé à mieux comprendre certains concepts en Kotlin et en architecture MVP.
---

## Navigation dans l'app
Ecran d'accueil (MainActivity)
↓ [Entrer]
Liste des notes (NoteListActivity)
↓                             ↓ [Clic note]
Editeur (NoteEditorActivity)  Editeur (NoteEditorActivity)

---

## Auteur

Myguel - 420-G25-RO Projet individuel
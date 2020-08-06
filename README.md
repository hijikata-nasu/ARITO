# ARITO
弁当箱を通して感謝の気持ちを伝えるためのAndroidアプリ  
パソコン甲子園2020応募作品  
チーム名:すとろべりーぱい  

# コーディング規約
基本的に以下のコーディング規約に従いましょう。  
https://dogwood008.github.io/kotlin-web-site-ja/docs/reference/coding-conventions.html  

変数名やメソッド名、クラス名などの命名はキャメルケースで書く  
変数名とメソッド名は最初小文字  
変数名は基本、名詞など  
メソッド名は動詞・目的語など  
クラス名は最初大文字  
例  
```kotlin
class HogeCalass
fun doSomthing()
var value
```
変数は基本的にvalを使う(変数は不変数にしましょう)  
non-null型を使いましょう  
lateinitやnullable変数は状況に合わせて  

関数などの括弧(波括弧{})は、その行に書く  
ifなども同等  
else ifはやelseはその前のifの閉じ括弧の行に空白一つ開けて書く  
1行ifなどは基本的に使用しない  
例
```kotlin
fun do() {
}

if (a == 1) {
} else if (a == 2) {
} else {
}

for (i in 0..2) {
}
```

メソッドに複数の引数がある場合は  
```kotlin
fun hoge(
    a: Int,
    b: String
)

hoge(
    10,
    "hoge"
)

fun foo(c: Int)
```
のようにしましょう。  
こうすることで引数の数や型がぱっと見で分かりやすいらしいです。


## GitHub運営方針
Git Flowをベースにしたものを使用します  
基本、ブランチは以下の種類がありそれを利用する  
- master
- develop
- feature/hoge

### master
完成系やある程度機能ができた時にマージされる  
先輩などに見せれるバージョンのアプリがいつでも使えるようにしておく  

### develop
開発中のアプリが置かれる  
ある機能が完成した時などにマージされる  
見せられるかもしれないけどバグを含んでいるかもしれない時  

### feature/hoge
ある機能を作成中のアプリが置かれる  
基本的に、このブランチをdevelopから生やして開発を進めていく  
`git checkout -b feature/hogeなどのブランチ名`←Developブランチやmaster以外の別ブランチに居る状態で行う  
hogeの部分は今開発中の機能名が来る(feature/searchなど)  
一番最初のcommitはこのブランチでは何をするかがメッセージとして書かれた空コミット(下のコマンドそのままでできます)   
`git commit --allow-empty -m "メッセージ(searchなど)"`  

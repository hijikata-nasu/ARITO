package com.nitok_ict.strawberrypie.arito

import android.bluetooth.BluetoothAdapter
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import android.view.View
import android.webkit.RenderProcessGoneDetail
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity(){
    companion object{
        //Bluetoothのリクエストコード
        private const val REQUEST_ENABLE_BT: Int = 1
        //デバイスのMACアドレス
        private const val macAddress: String = "00:06:66:84:E3:FB"
    }


    //ライフサイクル間でつかうView
    lateinit var bTConnectionTextView: TextView
    lateinit var batteryLifeTextView: TextView
    lateinit var messagePlayTimeTextView: TextView
    lateinit var serviceMessenger:Messenger
    //MaterialCardView
    lateinit var messageCard: MaterialCardView

    //bindServiceのインスタンスを格納するオブジェクト
    private lateinit var obentoSensorService: ObentoSensorService
    private var isServiceReady = false

    //ObentoSensorServiceと良い感じに通信する為のインターフェースを実装した関数オブジェクト
    private val connection = object : ServiceConnection {
        //Serviceとの接続が確立されたときに呼び出される
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ObentoSensorService.ObentoSensorBinder
            obentoSensorService = binder.getService()
            isServiceReady = true
        }
        //Serviceとの接続が失われた時に呼び出される
        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceReady = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //各種テキストビューの初期化
        bTConnectionTextView = findViewById(R.id.textview_BTconnection)
        batteryLifeTextView = findViewById(R.id.textview_batterylife)
        messagePlayTimeTextView = findViewById(R.id.textview_message_playtime)

        //MaterialCardView
        messageCard = findViewById(R.id.card_message)

        //ボタン一式
        val messageRecordButton: MaterialButton = findViewById(R.id.record_message_button)
        val retakingButton: MaterialButton = findViewById(R.id.retaking_button)
        val messagePlayButton: MaterialButton = findViewById(R.id.message_play_button)
        val connectButton: MaterialButton = findViewById(R.id.reconnection_button)

        //BluetoothAdapter
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        //ボタンの処理
        messagePlayButton.setOnClickListener {
            //TODO 画面遷移処理を書く
        }

        retakingButton.setOnClickListener {
            //TODO 画面遷移処理を書く
        }

        messageRecordButton.setOnClickListener{
            //TODO 画面遷移処理を書く
            Intent(this, MessegeEditActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }

        //Bluetoothが端末で使えるかの確認
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.toast_bluetooth_unsupported, Toast.LENGTH_LONG).show()
            this.moveTaskToBack(false)
        } else {
            //接続ボタンのクリックリスナーを設定
            connectButton.setOnClickListener {
                //接続試行中はボタンをグレーアウトし無効化
                it.isEnabled = false
                //接続を試行　失敗した場合はfalseが帰ってくるので else へ
                if (obentoSensorService.bluetoothConnect(bluetoothAdapter.getRemoteDevice(macAddress))){
                    //接続が成功したことを伝えるToast
                    Toast.makeText(this, R.string.toast_bluetooth_connection_success, Toast.LENGTH_LONG).show()
                    //接続済みと表示
                    bTConnectionTextView.setText(R.string.card_sensor_bluetooth_connected)
                    //デバイスのバッテリーの状態を確認
                    if (obentoSensorService.isBatteryLow()) {
                        //残りわずかと表示
                        batteryLifeTextView.setText(R.string.card_sensor_battery_life_low)
                    } else {
                        //OKと表示
                        batteryLifeTextView.setText(R.string.card_sensor_battery_life_high)
                    }
                    //接続済みになり必要なくなったのでボタンを非表示にする
                    it.visibility = View.GONE
                } else {
                    //接続が失敗したと伝えるToast
                    Toast.makeText(this, R.string.toast_bluetooth_connection_failure, Toast.LENGTH_LONG).show()
                }
                //ボタンのグレーアウトと無効化を解除　しかし、接続に成功したときは多分意味ない
                it.isEnabled = true
            }

            //Bluetoothがオフになっているときオンにしてもらう
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                //Bluetooth通信が可能な時
                Intent(this, ObentoSensorService::class.java).also { intent ->
                    startService(intent)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //起動しているObentoSensorService
        Intent(this, ObentoSensorService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (isServiceReady){
            if (obentoSensorService.isConnected()){
                bTConnectionTextView.setText(R.string.card_sensor_bluetooth_connected)
                if (obentoSensorService.isBatteryLow()) {
                    batteryLifeTextView.setText(R.string.card_sensor_battery_life_low)
                } else {
                    batteryLifeTextView.setText(R.string.card_sensor_battery_life_high)
                }
            } else {
                bTConnectionTextView.setText(R.string.card_sensor_bluetooth_unconnected)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if(true/*メッセージが記録されているか*/) { //TODO メッセージが記録されているかの確認の処理
            messageCard.visibility = View.VISIBLE
            messagePlayTimeTextView.text = ("null" + applicationContext.resources.getText(R.string.card_message_play_time_unit)) //TODO 再生時間を取得して格納する
        } else {
            messageCard.visibility = View.GONE
        }
    }

    override fun onStop() {
        //サービスをunbindする
        unbindService(connection)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO センサーからの通知を待機する関数を実装し呼び出す
        obentoSensorService.stopSelf()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK){
                // TODO Serviceを実装したら書く
            } else {
                Toast.makeText(this, R.string.toast_need_bluetooth, Toast.LENGTH_LONG).show()
                this.moveTaskToBack(true)
            }
        }

    }
}

/*

名取さな
　ああ名取さな
　　名取さな

    せんせえ
                                                                            ....+++++....
                                                                    ..-z77<<~___`` .`.`._~??71(..                                           ..-(((.
                                                              ..zz7<!__(((-..```` .JgQkkm..```` _?7i-.                                 ..-<<~..._(V
                                                         ..<__`` `` .dHgmqqqHm...dmqqkqqmHR.```````. ?7i,                           .?!_......._(f
                                                      ..<!_.```````.WgmmkkkkkkqkkkkkkkkqmgHr ````````..._C+.                     .,C_.........(df
        .-v7771+..                                  .+<__.`````````(HmqkkbbWWWWUUUUUWWWHHH@%````````````.._?G,                  J>...........(d=
        .X+_....._~<?<1-.                         .J!__ ``````````` ?HHkWWWWpppkXpbbbbbkHHD```````````````.._?I,             .J:...........-(Y`
          Cs__.........._1+.                    .v<__ ```````````````_THHkbbbbbkXbbbbkkHHY``````````````````._~?C+.         .3_..........-(v^
           ?Wx_............_?i.               .J<~_ ``````````````````` ?THHkbbkXbbkHH9=~````````````````````..~._j;       (>~........-_(v=`
             7k-.............._7<.           .>__.```````````````.```````` ?TWmHWHHB=~```````````.``````.`.`...~~.`(>    .J<;_......_(+z^
               7n._............._?o.        ,>~..`````````````.`````` .....((((Jz+++zzzzz&+(-.. `.....``..`....~~_`` .,  x::;_____(;+x>
                 ?G+-_..........._<zi.     ,>~~..`````````.````` .(+ZOttttOtllttttttttttrttttttrr&---..`.``. .~~~..`.(0.Z<:::;;;;;+z=
                   ?4x<-_........._;<zi   .I~~.. ```````.````..JOz<<<<<>>??====zllltttrtOz111zOtrtllOO+-..-~~~~~~....JVOC::~:;<+u7!
                      7z+<________(;;:<I  +<~~~...````````.(wC<<<:~:(<;<<+=1=?<<<<<<1lz111<;;:;<<1zttl=<<1-_~~.~.~(+<;;+z+++zzTl
                        ?Iz+<;::;;:::::?k.I~~~.....```. .JC<<~~~~(<<<<(+v<<(<<~:~~:~:<1<:<1?+_::~::~<11z>;<1+_~~(x<::;:<<<~~~~~_(.
                           ?Tu&+;;;:::~:jWI-_~.~~.._. .zz<~~~~((<<~~(<<<~~(<~~:~~~~~~~~<<~~<<1+_~~~~~~~?1+;;<1+(Z<::::::<~.......~.
                               _7Tw&++++<I;<?1-_.~~~(x<<~~~_(z<<~~(;<~~~~_<~~~~~~~~~~~~~~~~~_:(1-~~~~~~~~<1<;;>?I<::::::<-........(-
                                .z><~<<<<;;;:;;+1--jZ<:~~~(z<~~~_(<<~~~~~~~~~~~~~~~~~~~~~~__~_~~?+_.~~~~~~_?z>;;;<<:~:~:~1<........(v<<<_-...
                              .z!....~~~(;::::::;zZI<~~~(+<<__~~(<~~~~~~~~~~~~~~~~~~.~~.~~_1-~~~~(1_~~.~~.~~<1<;;;_<_:::~_1<.......(>:~~_..._?:.
                             .>........._;:::::(+>+<~~~(v<~(<~~~__~~..~~__~~~.~.~~.~~~.~~~~_1<~<<~(1-~~~~~~~~(1+;;;_(+zO+-_1-......_z~~~~_......._?1(.
                       .(+<<j>........._+<::::(v<<:~~_+>~~+v~~~_<~~~_~~~(<~~_~~~~.~~~~~~~~~~_1-_1_~_1<~~.~.~.~(1+;;;_(<;~<<1z-_....(o_~~.~~__........__?<..
                ... __._~::(v_........_J:(;:~(z~_<~~(z<~~(v~~~_<~~~(>~._<~~~~~~~~~~~.~~.~~~~~(z_(z~~_1<~~.~~~~~(1<;;< (>...._1v1-_.(Zwk+-~~~:__..........._~?1.
             .(<_......~~~~(>........_+>._<++v~_:~~(z~~~(x<~~.(<~~_+<~~(>~~~<~~.~~.~~~.~~.~~~~(:_+:~~_z-.~...~~~_1<~` `(_....(I_~?GzOld}?<&-_~<;_-__...___(;<+
         .-<~......._~~~~~~(o......._(>-(+z1z!_~~~(z~~~~(>~~~_<.~~(v~~~(~___<.._````````  _.~~_< _1_~.(I. `  _~~~(<     i-....(I_(JOllzh    ?TA++<;;;;<++us7^
    ..(?!_........___~~.-(v1k_.....-(l<<~__j{ ~~~~+:`   v~~~.(<` _(>~.~<   (~~_`           .~~~(_ +<~~~(:   ` ~~~~<-`    1-_-((d01?==ltZn       ??"""=?`
 .J>~..........__(;<~_(J>  dUl.._(+zI~....(C  ` _(v`   .>.~~~+_   (<~~~<   (<~    ` ` `   ` ~~~(: (>~~~(1_    _~.._1_ .  (Zf!` .c<??=lttZL.
 .(<;<________(;::++v7!   .ktw-Jv<_z<...._(`    .(:`  `(>~~~_z_`  +<~~~:`  (~~ `` `  ```   .~.~_1_(O_~~(+l ```._~~~(<_;<_ (~    1_<??=tttwh
   _7I&&++;+++++s7!       (SlOXo_.(I_....(>     _z_``` w<~~~(I_  _+~~~_;  .(_~~.~.~~.~.~~~~~~~~~z<(O<~~~<X<~.~_<~~~_;_(;;_ .    .I(>?=lttrZn.
          ~??!            dZlllzOuX+-___-J` `. ~(C_...(Z~~~~(I~~~~z_~~(I~~~(~~~~~~~~~~~.~.~~~~~:j>(zI_~~(dI~~~~1<~~~;<_<<:_ .    (<+?=zttttwk.
                         .ktll=??<<Z7!!?W%` .(~~(>~~~~(0~~~_dl~~:(z<~~(Z_~~(~~~~~~.~~~~~~~~~~~::jI(dX<:~:dR_~~~+>:~~;>_(z;< z;    I+<+1lttltwS,
                        .dZttt=??<(Z`  .X: __<~~j>~~~~(I:~~(Xl~:;;z>~~(k<::<<~~~~~~~~~~~~~~~~::;jI(w1I;;:jWc:::+z;;;;><(z<;_(L    .Iz>+=ltllOwZl
                        J0tttl=??<(C   .0 ~~(>~_z<~~~_jI:::+Wl(;;;dI:;+k<:;;<:~~~~~~~~~~~:::;;:;jI?l<z<;;jUk;;;+wz;;;;<(+I;<.r.    (I<;+1llllOwc1.
                       .Xrtttl=??<j{   .3.:~(>~(z<<:;;jI;;;jH2:;;;dI;>+HI;;;<:::::;::;;;;;;<<;;;dI?z:jz;;+IXz>>+XZ;;;;>_<O>;_z[     jc;;+=l==lOX-?-
                       dZttttl=??<w`   ({(::(>~(>>;;;;jI>>>jWI<>??dR+++WR>>><:;;;;;;;;;;;;;+z;>+dI1v:<I>>+>dI=?j00>;>>?<(Oz;_jR      T+;<+====zwI .>
                      jSOtttl=?>+jC    (!(;:+I~(>>;;>>dR???zZ$+z??XWIzzXWz>><_;;>>>>>;>;;;;jI;>+wIzVO+r??z>(0==z0wz>>+z<_zz><(W_      O<:<+=??=twl  1
                     JUrOllllz?;+d:    (.;;;+I~(>;;>>?dR=?=dIX+tl=XXSzlvzI??><<???????????>zI>>j0zz<~(OIlwz(klzd$dv??jI<~jI?<(X-       1_~<????lOX-  1.
                    J1vtl==l=?>;jV`    J_;>;+I~(;;>??=XHO==dIdzwOOwvwI=w<X=??zz?=?====?=?=?dI??dO?v~.~jZlO~(ulzXCzI??zI<~jI??jd]       .G_:<??>1twI  .O.
                  .v!Jwl==ll=>;;d{     S >;;+I~_>>??=zWqIlzwVzIzwtwI(0lz>jI?=wZ============d0=zZOz>``_(ZOI_ ulOK>j0??dI<:j0??jOD         O<<??>+ztw_  (O-
                  z`.url====z::<V`    .S.<>>>t__+??=lzK4RttwI:zzwwwI;vwOI_zz=zWzll==ll====zXI=zIOI```-j0w>` zlX3~+0?=wz::j0??z>k         .G(<?>>1tr>``(lO,
                 J!`.Xtl=?=?<::j}     .$`(>>>z<~+==llwR(HOtwI:(XwWwI~<OOI_(XzlXRzllllll===dXIzZjt!```.jkI_` vd$~_+0?zZ<::j0?>z{d           4(+??>zr>  .1zS.
                .{` jwt=???<::(r      .0_(?>>z>~+=llld$;zRtwI~_(wdkk__<Xz:`(wlwWktllll==lzZdZw<z>````-jK!``.X3_` (I?zI<::jI?>z!J.           j++??ztz_` +vvn
               .I   OOOz>??<::J`      (Z;(???zI:<llltdC:~dkw$_` jXXW;`_?Xw.`(wtXZkOlllll=w10OC({._ ..(jhJ...C````(I?wz;:;zI>?z`()            ?o<>?zI_..(I?dl
               z>  .vtl?>>?<:j}       .ZI.<?>1O_(1lltZ>___Uw0_...dkJn.._?w{``-zw>?SOttllz>(XC.~~.&VY1dYTHNs?TSm{`(Ij0?;:;dv>+I .$             .n<+zO<..(I?+W,
              (O<  .rtI>>??<(\        (0z-<???O<:+lttZ>_. zHWY=<jgNHAvTWk+_``` ?l  ??777!`_~ ``_<~ (M# `(##N-._7hJOdC>;;<Wz?z>_.h              .4-<r<~~(O?<vS.
             .Zl< `.Otv>?=<+r         ($(l(???zI:<zltX>_(J9!~ (d##@` dm--```````````````````````` .MMMm(MNNMb   (HdD?>;;dR?+I;_ X                1_z>~_+I?<jw[
             j$z>_`.ztz>?<j%          (t+O<<z??w+;+ltdZH#! `` dNNNN,.dMN-``````````````````````` `(NMNN##NNNN_ `(XH$>;;jW01w??_ w                 1z<~_+v<~j>I
            .Xz1I_. +O=<<j%           d>+1G_Oz?zk<>1rdXM>.```.MMMNNNNMNMb `````````````````````` `JNNN####NM$```(DdI>;jfzIO0??<`j!                .k<~(zz<_+!<
            (R<?O<..+Z<<j\            X<??1GjuO=Zn<>zXk?%-`  .MMNN###NNNM_``````````````````````` JNMM#####NM:  <.0?>u0<Ov<k??< (_                .I<(+v<_(<_:
           .d0<+zo_~(O<J^            .K_???1wk<Czdk+>OW>_``   MNNNN###NMa,````````````````````````.?WHHHHHMM@``` JI1d9<~<:(0???_,)                ,I_<zz<~+c_/
           .fj<<1I<~(O(>`            .D(????1XI_??Xkz?Zk_`````JMMMMMMMMMM}````````````.``````` ````.WA.- (jV!``.(wdVC?<__:(S===<.$               .Z<(+z<.(d>.`
           (C(>_<zz__jZ              ,$(?????d>:_~(vUylZI_.``  `(H9Y7TTHD`````` ```......`` ````````_7TXWY=!....XW0=?<~`_~(S==?< S>              J<:><<__jC_!
           (>_;__+O<_(X              dt(?????dI(_._<?vXwZn-...  ?UA+.Jz=```````` ``......``` ``````.........._J=~Xl=?< `(~j$??=z_do             .>:;<~.-J1z!
            ~-(<_(1z<_z{             d}(????=dS~~  (??=dWXX,.......__.``````` `````  ._. ````` ````..........__.(0=??!  (~dI===?_jZ.           .C~:~_.(v(?
             _(O<.(1z<(w.            X:<????1wS_:.  +?=zS?WWn.........``````` ``` ``````````` ```````..........-XI=?<`` :~dI?===<.k:          .v.~._(7` `
             .-jI_.(+<:(O.          .W!<??==zwd:(_ `(===d0TI_?=<....``.````` `` `` `````` `` ``````````..`..`..jD===>  (<(XZ====1_Xz.        ._..(c!
             .1(OI-._<;:(G.         (K_>?===zZwI_<_`.+?=zX:(+-..`.``.````````` ` `` ` ` `` ```` ````````````` (HI=?<<.-z<(Zuz====<(Z_         ```
               ?OvX+. ~:~~1.        wD.?====dZlX-<+. (?==vk+Jo ````````````` ``````` ``` ``` ``` `` `````````(WD==?<~.(Z~wldZ====z.S>
                 ?-(1&-._._?,      .d$.===llwZlOl(O<.~<===Xx(de.`````````` `` ` `````````````` `` ````````` (vWI=?<~~(dI(Zlw0=====_zl
                      ?T&-..._     (X}(=l==zuOllw<zI_~_<==zWVZZn.```````` `````` `_<<~__~!!<( ```` `` ``` .vwd0=?<<~(+XIz1=zXI====<.k;
                          `~`!     wK<+==l=z0lllOI(S>_~~<1?vklllXo. ``` ``` ` ` ````````` ```````````` .+VOlOX1?<<~(vdkzI?=zdk====< X>
                                  .dR(+===lw0l=llwoXk++~~<?=dktlltwWA-.``` ``` `` `` ` ``````` `` ` .JU0ttttXC><<~(ZzXRXI===zXv<<<<_(Z-
                                  ,dD~+=l=ldZl=llwudWIOo__<>?XytltdWZlOO+.. `````` `` `` ` ````` .(ZXWktltrd3;;<_(wOZXKXO=?==wI<~~_ .Rz
                                  zWC_+===zZOl=lzuOXWzzXo_~<;+UOlldWOllltttO&-. ``````` ```` ..JZOtttXktttwC:<<_jVOZOWIOzz===zk__    dZ-
                                 .wk:_;<>?zSl=llwVlzWROOZk-~~:<OOtdWOtttttttlOv<<-.. ``` ..JC<<WtttttXWOtwC::_(v1zOld0llwO===lX:     ,Rz.
                                 ,w3`_~::<d0l=ltwZl=vWOzXzOz-::(zwXStlltlttttwI::::<<<zCC<::::<UWAtttwfkZz::((xOOXOwZlllOXz===zI  `   Xz-
                                 zrI  __~(dI==lOuOlllzXzwlOO+-~:<1UkttttttwV=<I:::::::::::::::<I_?WAwwVIzu&wUOtltzZtlttllwZ<<<<1_`  ``(Zl_
                                .OOr    _(XI=llw0llllllwZllllwUX&&zzUAwwOv~..(I::::::::~~~~~~~_z__?7TWXHUOtrttlllOwltllllOX<~_ .l     .XO1.
                                JOd%     (0=lltwZlltlttwOlltltrttwHUUUX$_._..($:::::~~~.~.~~~~~j_._..(wVZttrOtlllOvttttlllw:`   j<_~~~_jZl}
                                OzW!     JZ=llOZOtttlltvOtttltrttdyZttX:..(-J9<~~~~............_>(>.._wWStttrttlllOrttllllOl  ` .w<(++<_Sl>
                               .OwR   ` .w1=llw0tttttltvtttttrtttdSttw3...jV!..................`.z~...j-(XttrtltlllrtOz1?<<1<. ..zI+???<jZz
                               Ozdt  `` jC<+1zXZttltttrrtttttrrtOX7CV!....(<.........````..`.`` (>..`._1+?wOrOllllllv<<<~~_(O<++?zXz===1_Wz>
                              .1wX: .--(Z!`__juOttttttrttltttwwOC_v!......-<_.`..`.``````````...>`   `` ?<(z?7OOz==lz_..```-jO====Zk===l<jk=-
                               jwk__:(<zC    (ZlttttlttOlzOvC!__.(_. `` `  _<`````````.``.````.<        .l___..._??zt+_---_(+wz===zXI===z_WOz
                              ,td$(;;>+w~    (11zlllOwwVC!.......(<.        (-.``````````````.<    `  ` .O__........._?1&+&+?zXz===zXz===<(Rl<{
                             .ttd{;>>>zI ` `.<_~<<+Z=_....~~~~~~(<1_         <-`````````````.<`   `    .(1<~~~~.__-....-wl._?1wwz===Ok===<_WO=}
                             Jttk_>>>+wz(_--(<-<~_(I__~~~~~~~~~~(>(<- `     ` 1.```````````.<``       .(<(<_~~~~~~~~~~~_(R_.`  __?I?=wI??<_(RI1.
                            (ttd$(>??zVl=zC!__....(t~~~~~~~~~~~~j>_1-        `-I.```````` .>        ` -z__<<:~~:~:~~:~:~(H_.(~..` jz?zXz??<_4kzi.
                           .ttId><>?1wOlz{  .-__..(C::~~~~~~~~~~j>.-1-    `     ?i.```` .?`       ` ``(>.-1<::::::::::::(W<(~...  .S=?zwz?<_(HO=_
                          .wrtwf_>>?zZ=zf`` ..-<-.j>:~:~:~~~~~~(z>.._1.`         -(?i .+&-   `      `.v..-+<::::::::~:~:(u+!...`` `?k=?wO?<_.dkz?..
                          dVtwX{(>?1wI?d\ `.....__d<~:::~:::~~:(z:..._<-`     `.J!_<(+++<?7&.`     ` (>...(>:::::::::::~(X>....     zI?1uz><_.Wwz?`
                         ,XZOXD`(??zI?j$    ....._r<::~::~::~::(X<.... 1.`   ` (<<-.<X0<.-J>?o.  `  .2....(I:::::~::~:~:~X>...       Oz?Ow+;__(kO?>-
                        .oXtwW% >>1zv1Z`     ....(Z:~::~::~~:~~(k<....`.O.` ` ,>_-__<?<<<~__.<1-`   J!... (Z::::::~::~_._w:..`       .S?1wI;<_ wzz11.
                        (wZrd#`.??dZ?z!       ...(I:~~:~~::~:::jS~.```. jh.  .>_<(~(_ .<_<_<.(_ji` (C...   O<::::~:~~_..-w:...       `(Z?zXz<_ (Swz?1
*/

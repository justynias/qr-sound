package com.kozik.justyna.qrsound.di


//@Module
//class QrSoundModule {
//
//    @Provides
//    @Singleton
//    fun provideQrSoundRepository() : QrSoundRepository {
//        return QrSoundRepository()
//    }
//}
//
//
//
//class AppModule(private val app: Application) {
//    @Provides
//    @Singleton
//    fun provideContext(): Context = app
//}
//
//@Singleton
//@Component(modules = [AppModule::class])
//interface AppComponent
//
//lateinit var wikiComponent: AppComponent
//
//private fun initDagger(app: Application): AppComponent =
//        DaggerAppComponent.builder()
//            .appModule(AppModule(app))
//            .build()
//
//

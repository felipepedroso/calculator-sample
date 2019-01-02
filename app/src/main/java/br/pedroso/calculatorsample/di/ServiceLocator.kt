package br.pedroso.calculatorsample.di

object ServiceLocator {
    private val servicesMap = mutableMapOf<String, Any>()

    fun registerService(serviceInstance: Any) =
            registerService(serviceInstance.javaClass.name, serviceInstance)

    fun registerService(tag: String, serviceInstance: Any) {
        servicesMap[tag] = serviceInstance
    }

    fun <T : Any> registerService(serviceClass: Class<T>, serviceInstance: T) {
        registerService(serviceClass.name, serviceInstance)
    }

    fun <T> getService(serviceClass: Class<T>): T? = getService(serviceClass.name)

    fun <T> getService(tag: String): T? = servicesMap[tag] as? T

    fun <T : Any> getOrRegisterService(tag: String, instantiationAction: () -> T): T {
        val service: T? = getService(tag)

        if (service != null) {
            return service
        }

        val newService = instantiationAction.invoke()

        registerService(tag, newService)

        return newService
    }

    fun <T : Any> getOrRegisterService(serviceClass: Class<T>, instantiationAction: () -> T): T =
            getOrRegisterService(serviceClass.name, instantiationAction)

    fun clearRegisteredServices() = servicesMap.clear()
}
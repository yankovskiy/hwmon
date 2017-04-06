# hwmon
Модуль для мониторинга состояния SAN-оборудования интегрирующийся с системой мониторинга xymon.

Поддерживаются дисковые массивы Infortrend. Мониторинг осуществляется через snmp
* DS3048RE
* DS3024RB

Поддерживаются дисковые массивы Dell. На системе должен быть установлен mdstoragemanager
* MD3820F

Список мониторинга задается в xml-файле, пример смотрите в [config.xml](https://github.com/yankovskiy/hwmon/blob/master/config.xml).

Использование:

`java -jar hwmon.jar config.xml`
> для запуска из системы мониторинга

`java -jar hwmon.jar config.xml -dump`
> для тестового вывода всех устройств мониторинга

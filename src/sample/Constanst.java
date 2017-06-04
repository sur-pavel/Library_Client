package sample;

public final class Constanst {
    final static String[] fieldsName = {
            "10: ISBN, Цена",
            "11: ISSN",
            "19: Идентификационный номер нетекстового материала",
            "101: Язык основного текста",
            "102: Страна",
            "106: Физические характеристики - Бумажный носитель",
            "110: Кодированная информация (периодическое издание)",
            "115: Физические характеристики - Видеозапись",
            "116: Кодированные данные.Графические материалы",
            "117: Кодированные данные. Трехмерные искусственные объекты",
            "123: Физические характеристики - Картографический материал",
            "125: Физические характеристики - Музыкальное воспроизведение (ноты, аудио)",
            "126: Физические характеристики - Звукозапись",
            "130: Физические характеристики - Микроформа",
            "135: Физические характеристики - Электронный ресурс",
            "140:Кодированные данные",
            "141:Кодированные данные",
            "200: ОБЛАСТЬ ЗАГЛАВИЯ : Заглавие, дополнительные данные",
            "205: Сведения об издании",
            "210: Выходные данные",
            "215: Количественные характеристики",
            "225: Область серии",
            "230: Электронный ресурс",
            "239: Служебное поле-разделенные сист. требования к ЭР",
            "300: Общие примечания",
            "314: Примечание об интеллектуальной ответственности",
            "316: Примечания об особенностях экземпляра",
            "317: Примечание о происхождении экземпляра",
            "318: Примечания о действиях по сохранности док-та",
            "320: Примечание о наличии библиографии",
            "390: Список библиографии",
            "327: Примечание о содержании",
            "328: Примечание о диссертации",
            "330: Содержание (оглавление)",
            "331: Аннотация",
            "337: Примечания об электронном ресурсе",
            "391: Примечание о наличии автографов",
            "395: Примечание - Пометки автора",
            "396: Примечание - Пометки владельца коллекции",
            "397: Примечание о технике изготовления предмета",
            "398: Примечание - Экслибрис",
            "399: Примечание - Состояние (сохранность) предмета",
            "421: Самостоятельное приложение",
            "422: Издание, к которому относится  приложение",
            "423: Издается в одной обложке с настоящим СИ",
            "430: Предыдущие заглавия настоящего издания",
            "440: Последующие заглавия настоящего издания",
            "451: Параллельное издание на том же носителе",
            "452: Параллельное издание на другом носителе",
            "454: Оригинал переводного издания",
            "461: Из общей части многотомника - Основные сведения",
            "46: Из общей части многотомника (Продолжение) - дополнительные данные)",
            "463: Издание, в котором опубликована статья",
            "470: Рецензия/Реферат на ...",
            "481: В томе подборки также переплетены ...",
            "488: Другие связанные издания",
            "503: Унифицированный заголовок формы",
            "510: Параллельные заглавия",
            "517: Разночтение заглавий",
            "541: Перевод заглавия",
            "60: Раздел знаний",
            "600: Персоналия (о нем) - имя лица",
            "601: Персоналия (о нем) - коллектив",
            "606: Предметная рубрика",
            "607: Географическая рубрика",
            "610: Ненормированные ключевые слова",
            "619: Служебное поле-разделенные подрубрики",
            "621: Индексы ББК",
            "629: Краеведение",
            "675: Индексы УДК",
            "686: Индексы другой классификации",
            "690: Издательский индекс",
            "691: ВУЗ (Учебная литература)",
            "692: ВУЗ (Коэффициент КО)",
            "694: Заявка на учебную литературу",
            "700: 1-й индивидуальный автор",
            "701: Другие индивидуальные авторы",
            "702: Редакторы, составители, композиторы ...",
            "710: 1-й коллектив (организация) - заголовок описания",
            "711: Другие коллективы, не входящие в заголовок описания",
            "900: Коды - тип, вид, характер документа",
            "901: БО журнала - Сведения  о заказанных экземплярах",
            "902: Держатель документа",
            "903: Шифр документа в БД",
            "904: Шифр приложения, хранящегося отдельно",
            "905: Настройка.Тираж Осн. КК. Доп. добавочные КК",
            "906: Систематический шифр",
            "907: Каталогизатор, дата",
            "908: Авторский знак",
            "909: БО журнала - Зарегистрированы поступления",
            "910: Сведения  об ЭКЗЕМПЛЯРАХ",
            "911: Язык резюме",
            "912: Примечания о языке",
            "915: Физические характеристики - Визуально-проекционный материал",
            "916: Физические характеристики - Кинофильм",
            "919: Язык документа (дополнительные данные)",
            "920: Имя рабочего листа",
            "921: Транслитерированное заглавие",
            "922: Другие (2-я и следующие статьи сборника)",
            "923: Выпуск, часть (Номер-заглавие)",
            "924: \"Другое\" заглавие",
            "925: Другие (2-й и последующие тома в одной книге)",
            "926:\"Другие\" авторы",
            "929: Особенности экз-ра (разъединенные данные)",
            "930:Кумулированные номера за другой год",
            "931: Регистрация журнала - Дополнения к номеру, Начальный N в году",
            "932: Служебное поле: N статьи для копирования",
            "933: Отдельный номер журнала - Шифр общего описания",
            "934: Регистрация журнала - Год",
            "935: Регистрация журнала - Том (если есть)",
            "936: Регистрация журнала - Номер, Часть ...",
            "937: Регистрация журнала - Технологический путь",
            "938: БО журнала - Сведения  о заказах (поквартальные)",
            "939: Регистрация журнала - Приложение к номеру (хранится отдельно)",
            "940: Архивные сведения - списание",
            "941:Архивные сведения - проверка фонда",
            "950: Сведения о графических данных",
            "951: Сведения о внешнем объекте",
            "953: Внутренний двоичный ресурс",
            "961: Из общей части мн-ка или источника статьи - Авторы, редакторы ...",
            "962: Из общей части мн-ка или источника статьи - Коллективы",
            "963: Издание, в котором опубликована статья (Продолжение - см.поле 463) - Сведения о серии и др.",
            "964: Индексы ГРНТИ",
            "965: Дескрипторы",
            "971: 1-й Врем.коллектив - заголовок описания",
            "972: Другие врем.коллективы, не входящие в заголовок описания",
            "981: Издатель директивного(юридического) или нормативно-технического документа",
            "982: Патенты, Отчеты о НИР, НТД и ЮД - специфические сведения",
            "990: БО журнала - Признак ручной/автоматической кумуляции",
            "993: Копия - микрофильм/микрофиша",
            "998: Импорт - служебное поле",
            "999: Количество выдач",
            "1909: Архивные сведения - списание",
            "1: Идентификатор записи",
            "5: Идентификатор версии",
            "12: Идентификатор Фингерпринт",
            "14: Идентификатор статьи",
            "20: Номер документа в национальной библиографии",
            "21: Номер государственной регистрации",
            "22: Номер публикации органа государственной власти",
            "35: Идентификатор записи, полученной из другой системы",
            "100: Данные общей обработки",
            "105: Коды: текстовые материалы, монографические",
            "122: Коды: период времени содержания документа ...",
            "207: Специфические сведения о материале ...",
            "211: Запланированная дата издания",
            "301: Примечания, относящиеся к идентификационным номерам",
            "302: Примечания, относящиеся к кодированной информации",
            "305: Примечания о дате основания издания",
            "309: Примечания об основном источнике информации и ...",
            "311: Примечания к полям связи ...",
            "313: Примечания, относящиеся к тематическому доступу",
            "321: Примечания об отдельно изданных указателях ...",
            "324: Примечание о первоначальной версии",
            "325: Примечание о копии",
            "326: Примечания о периодичности ...",
            "333: Примечания об особенностях распространения и использования",
            "336: Примечания о виде электронного ресурса",
            "462: Уровень поднабора",
            "500: Унифицированное заглавие",
            "501: Унифицированное общее заглавие",
            "518: Заглавие в современном правописании",
            "530: Ключевое заглавие (Сериальные издания)",
            "531: Сокращенное заглавие (Сериальные издания)",
            "532: Расширенное заглавие",
            "540: Дополнительное заглавие ...",
            "545: Заглавие части",
            "602: Родовое имя как предмет",
            "604: Имя и заглавие как предмет",
            "605: Заглавие как предмет",
            "608: Форма, жанр, физические характеристики документа как точка доступа",
            "615: Предметная категория",
            "620: Место как точка доступа",
            "660: Код географического региона (GAC)",
            "661: Код периода времени",
            "676: Десятичная классификация Дьюи (DDC/ДДК)",
            "680: Классификация Библиотеки Конгресса (LCC/КБК)",
            "712: Наименование организации-вторичная интеллектуальная ответственность",
            "720: Родовое имя-первичная интеллектуальная ответственность",
            "721: Родовое имя-альтернативная интеллектуальная ответственность",
            "722: Родовое имя-вторичная интеллектуальная ответственность",
            "801: Источник записи",
            "802: ISSN центр",
            "830: Общее примечание, составленное каталогизатором",
            "856: Электронный адрес документа",
            "899: Данные о местонахождении",
    };
}

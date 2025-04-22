# Техническое задание

Продолжим работать над трекером задач. Вам предстоит добавить в него новую функциональность и провести рефакторинг написанного кода с учётом изученных принципов ООП.

В этом спринте вы изучили методы тестирования и библиотеку JUnit, и теперь существующий код нужно покрыть тестами, а для нового кода, который вам ещё предстоит добавить в проект, мы рекомендуем писать тесты сразу. Некоторые особенно важные для тестирования кейсы мы опишем ниже.

### Обобщаем класс «Менеджер»

При проектировании классов и их взаимодействия бывает полезно разделить описание функций класса на интерфейс и реализацию. Из темы об абстракции и полиморфизме вы узнали, что такое интерфейсы и как их использовать для выделения значимой функциональности классов (абстрагирования). При таком подходе набор методов, который должен быть у объекта, лучше вынести в интерфейс, а реализацию этих методов — в класс, который его реализует. Теперь нужно применить этот принцип к менеджеру задач.

1. Класс `<span class="code-inline__content">TaskManager</span>` станет интерфейсом. В нём нужно собрать список методов, которые должны быть у любого объекта-менеджера. Вспомогательные методы, если вы их создавали, переносить в интерфейс не нужно.
2. Созданный ранее класс менеджера нужно переименовать в `<span class="code-inline__content">InMemoryTaskManager</span>`. Именно то, что менеджер хранит всю информацию в оперативной памяти, и есть его главное свойство, позволяющее эффективно управлять задачами. Внутри класса должна остаться реализация методов. При этом важно не забыть имплементировать `<span class="code-inline__content">TaskManager</span>`, ведь в Java класс должен явно заявить, что он подходит под требования интерфейса.

### Подсказки

Что делать с новым классом InMemoryTaskManager

В `<span class="code-inline__content">InMemoryTaskManager</span>` нужно скопировать бывшее содержимое класса `<span class="code-inline__content">TaskManager</span>`. Чтобы класс реализовывал интерфейс, необходимо после его названия указать ключевое слово `<span class="code-inline__content">implements</span>` и имя интерфейса — `<span class="code-inline__content">class InMemoryTaskManager implements TaskManager</span>`. Перед реализацией методов интерфейса нужна аннотация `<span class="code-inline__content">@Override</span>`.

Как быстро сделать TaskManager интерфейсом

### История просмотров задач

Добавьте в программу новую функциональность — нужно, чтобы трекер отображал последние просмотренные пользователем задачи. Для этого добавьте метод `<span class="code-inline__content">getHistory</span>` в `<span class="code-inline__content">TaskManager</span>` и реализуйте его — он должен возвращать последние 10 просмотренных задач. Просмотром будем считать вызов тех методов, которые получают задачу по идентификатору, — `<span class="code-inline__content">getTask(int id)</span>`, `<span class="code-inline__content">getSubtask(int id)</span>` и `<span class="code-inline__content">getEpic(int id)</span>`. От повторных просмотров избавляться не нужно.

Пример формирования истории просмотров задач после вызовов методов менеджера:

[]()![](https://pictures.s3.yandex.net/resources/Untitled-146_1702305529.png)

У метода `<span class="code-inline__content">getHistory</span>` не будет параметров. Это значит, что он формирует свой ответ, анализируя исключительно внутреннее состояние полей объекта менеджера. Подумайте, каким образом и какие данные вы запишете в поля менеджера для возможности извлекать из них историю посещений. Так как в истории отображается, к каким задачам было обращение в методах `<span class="code-inline__content">getTask</span>`, `<span class="code-inline__content">getSubtask</span>` и `<span class="code-inline__content">getEpic</span>`, эти данные в полях менеджера будут обновляться при вызове этих трёх методов.

Обратите внимание, что просмотрен может быть любой тип задачи. То есть возвращаемый список задач может содержать объект одного из трёх типов на любой своей позиции. Чтобы описать ячейку такого списка, нужно вспомнить о полиморфизме и выбрать тип, являющийся общим родителем обоих классов.

### Подсказки

Как отобразить в коде историю просмотров

История просмотров задач — это упорядоченный набор элементов, для хранения которых отлично подойдёт список. При создании менеджера заведите список для хранения просмотренных задач. Этот список должен обновляться в методах `<span class="code-inline__content">getTask</span>`, `<span class="code-inline__content">getSubtask</span>` и `<span class="code-inline__content">getEpic</span>` — просмотренные задачи должны добавляться в конец.

Обновление истории просмотров

Учитывайте, что размер списка для хранения просмотров не должен превышать десяти элементов. Если размер списка исчерпан, из него нужно удалить самый старый элемент — тот, который находится в начале списка.

Где пригодится тип списка просмотренных задач

Для списка просмотренных задач нужен тип `<span class="code-inline__content">Task</span>`. Метод `<span class="code-inline__content">getHistory</span>` должен возвращать список именно такого типа. В итоге он будет выглядеть так — `<span class="code-inline__content">List<Task> getHistory()</span>`.

### Утилитарный класс

Со временем в приложении трекера появится несколько реализаций интерфейса `<span class="code-inline__content">TaskManager</span>`. Чтобы не зависеть от реализации, создайте утилитарный класс `<span class="code-inline__content">Managers</span>`. На нём будет лежать вся ответственность за создание менеджера задач. То есть `<span class="code-inline__content">Managers</span>` должен сам подбирать нужную реализацию `<span class="code-inline__content">TaskManager</span>` и возвращать объект правильного типа.

У `<span class="code-inline__content">Managers</span>` будет метод `<span class="code-inline__content">getDefault</span>`. При этом вызывающему неизвестен конкретный класс — только то, что объект, который возвращает `<span class="code-inline__content">getDefault</span>`, реализует интерфейс `<span class="code-inline__content">TaskManager</span>`.

Подсказка про getDefault

Метод `<span class="code-inline__content">getDefault</span>` будет без параметров. Он должен возвращать объект-менеджер, поэтому типом его возвращаемого значения будет `<span class="code-inline__content">TaskManager</span>`.

### Сценарий для проверки

Начиная с этого урока обязательными для проверки кода становятся юнит-тесты. Сценарии, представленные в этом разделе ТЗ, опциональны. Вы можете реализовать их для более простой демонстрации возможностей программы.

Например, в главном классе можно реализовать такой несложный сценарий:

* Создать несколько задач разного типа.
* Вызвать разные методы интерфейса `<span class="code-inline__content">TaskManager</span>` и напечатать историю просмотров после каждого вызова. Если код рабочий, то история просмотров задач будет отображаться корректно.

Вы можете использовать этот код (с небольшими модификациями конкретно под ваш класс `<span class="code-inline__content">TaskManager</span>`).

Скопировать кодJAVA

```
private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    } 
```

### Сделайте историю задач интерфейсом

В этом спринте возможности трекера ограничены — в истории просмотров допускается дублирование, и она может содержать только десять задач. В следующем спринте вам нужно будет убрать дубли и расширить её размер. Чтобы подготовиться к этому, проведите рефакторинг кода.

Создайте отдельный интерфейс для управления историей просмотров — `<span class="code-inline__content">HistoryManager</span>`. У него будет два метода: `<span class="code-inline__content">add(Task task)</span>` должен помечать задачи как просмотренные, а `<span class="code-inline__content">getHistory</span>` — возвращать их список.

Объявите класс `<span class="code-inline__content">InMemoryHistoryManager</span>` и перенесите в него часть кода для работы с историей из класса `<span class="code-inline__content">InMemoryTaskManager</span>`. Новый класс `<span class="code-inline__content">InMemoryHistoryManager</span>` должен реализовывать интерфейс `<span class="code-inline__content">HistoryManager</span>`.

Добавьте в служебный класс `<span class="code-inline__content">Managers</span>` статический метод `<span class="code-inline__content">HistoryManager getDefaultHistory</span>`. Он должен возвращать объект `<span class="code-inline__content">InMemoryHistoryManager</span>` — историю просмотров.

Проверьте, что теперь `<span class="code-inline__content">InMemoryTaskManager</span>` обращается к менеджеру истории через интерфейс `<span class="code-inline__content">HistoryManager</span>` и использует реализацию, которую возвращает метод `<span class="code-inline__content">getDefaultHistory</span>`.

Ещё раз всё протестируйте!

### Покрываем проект тестами

Вы прошли тему о юнит-тестировании и теперь можете перейти к полноценной автоматизированной проверке вашего кода. Вам нужно протестировать все созданные классы проекта.

Для этого вам потребуется:

* **Набор библиотек тестирования.** Здесь вам поможет инструкция ниже и второй урок темы «Unit-тесты», а точнее — пошаговая инструкция по добавлению в проект библиотек JUnit.
* **Анализ технического задания.** На основе этого и предыдущего задания (финального проекта четвёртого спринта) сформулируйте ключевые моменты работы классов `<span class="code-inline__content">Manager</span>` и `<span class="code-inline__content">Task</span>` и основные требования к их функциональности.
* **Организация кода.** Для каждого класса создайте соответствующий тест-класс в пакете внутри каталога `<span class="code-inline__content">test</span>`. Если его не подсвечивает IDEA, кликните правой кнопкой мыши и выберите пункт Mark Directory as → Test Sources.

Добавьте JUnit в проект (инструкция со скриншотами)

Прежде чем приступать к написанию тестов, добавьте поддержку JUnit в проект. Для этого выполните в IntelliJ IDEA следующие действия.

1. Откройте любой класс, например `<span class="code-inline__content">Epic</span>`.
2. Нажмите `<span class="code-inline__content">Ctrl</span>`+`<span class="code-inline__content">Shift</span>`+`<span class="code-inline__content">T</span>` (`<span class="code-inline__content">Shift</span>`+`<span class="code-inline__content">Cmd</span>`+`<span class="code-inline__content">T</span>` для macOS). В выпадающем меню выберите пункт **Create New Test** (англ. «Создать новый тест»). В появившемся окне нажмите кнопку **OK** — тест будет размещён в папке `<span class="code-inline__content">test</span>`, если каталог `<span class="code-inline__content">test</span>` отмечен как Test Sources.
   []()![](https://pictures.s3.yandex.net/resources/S7_09_1702305774.png)
3. В меню выбора теста (**Testing library**) выберите **JUnit5**, а затем нажмите кнопку **Fix** (англ. «Исправить»).
   []()![](https://pictures.s3.yandex.net/resources/S7_10_1702305793.png)
4. Скачайте библиотеку в папку **lib**. Поставьте галочку около пункта **Download to** (англ. «Скачать в...») и нажмите кнопку **OK**, чтобы подтвердить создание теста.
   []()![](https://pictures.s3.yandex.net/resources/S7_11_1702305793.png)
5. После этого откроется файл **EpicTest**. Можно переходить к написанию тестов. Проверьте, что все библиотеки загрузились в папку lib.
   []()![](https://pictures.s3.yandex.net/resources/S7_12_1702305793.png)

Не нужно покрывать тестами весь код. Даже для такого небольшого проекта тестирование может занять значительное время. Но всё же обратите внимание на некоторые нюансы, которые необходимо проверить:

* проверьте, что экземпляры класса `<span class="code-inline__content">Task</span>` равны друг другу, если равен их `<span class="code-inline__content">id</span>`;
* проверьте, что наследники класса `<span class="code-inline__content">Task</span>` равны друг другу, если равен их `<span class="code-inline__content">id</span>`;
* проверьте, что объект `<span class="code-inline__content">Epic</span>` нельзя добавить в самого себя в виде подзадачи;
* проверьте, что объект `<span class="code-inline__content">Subtask</span>` нельзя сделать своим же эпиком;
* убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
* проверьте, что `<span class="code-inline__content">InMemoryTaskManager</span>` действительно добавляет задачи разного типа и может найти их по `<span class="code-inline__content">id</span>`;
* проверьте, что задачи с заданным `<span class="code-inline__content">id</span>` и сгенерированным `<span class="code-inline__content">id</span>` не конфликтуют внутри менеджера;
* создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
* убедитесь, что задачи, добавляемые в `<span class="code-inline__content">HistoryManager</span>`, сохраняют предыдущую версию задачи и её данных.

А вообще — смело тестируйте самостоятельно всё, что посчитаете нужным. Но не забывайте о том, что нужно покрыть тестами основные методы менеджера и самих задач. Тестов много не бывает!

Подсказка: примеры тестов

Тест создания задачи.

Скопировать кодJAVA

```
@Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    } 
```

Тест добавления в историю.

Скопировать кодJAVA

```
@Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    } 
```

💡 При создании тестов может потребоваться дополнительная подготовка. Подумайте, какие действия могут понадобиться перед проверкой менеджера.


By Conrad Lee — ByteDance Growth Camp Assignment
本项目模拟抖音消息体系的核心能力，包括：消息列表、好友会话、置顶、备注、搜索、聊天界面、系统消息、动态插入、弱网体验、数据埋点、可视化数据看板等功能。

采用 MVVM + Repository + SQLite 架构，支持持久化、可扩展、模块化开发。

## 📂 目录

- [项目结构 (Architecture Overview)](#项目结构-architecture-overview)
- [核心功能 (Features)](#核心功能-features)
  - [消息列表页](#消息列表页)
  - [聊天页面](#聊天页面)
  - [好友备注](#好友备注)
  - [消息搜索](#消息搜索)
  - [弱网体验](#弱网体验)
  - [动态消息中心](#动态消息中心)
  - [数据看板（自由探索亮点）](#数据看板自由探索亮点)
- [附加功能 (Features)](#附加功能-features)
  - [全国天气](#全国天气)
  - [登录注册](#登录注册)
- [技术难点与解决方案 (Key Challenges)](#技术难点与解决方案-key-challenges)
- [数据库设计 (SQLite Schema)](#数据库设计-sqlite-schema)
- [消息中心 / 动态插入机制](#消息中心-动态插入机制)
- [运行方式 (How to Run)](#运行方式-how-to-run)
- [截图展示 (Screenshots)](#截图展示-screenshots)
- [License](#license)

## 🏗 项目结构 (Architecture Overview)

本项目采用 **MVVM + Repository + SQLite** 的架构设计，将各类业务按 Feature 模块切分，便于扩展与重构。

```text
app/
 ├── core/
 │     ├── db/                # SQLite 数据库相关（DAO + Repository + Helper）
 │     ├── metrics/           # 埋点系统（事件上报、行为分析、可视化数据）
 │     ├── network/           # Retrofit 网络层（天气模块）
 │     └── utils/             # 通用工具类（时间、Json、Highlight 等）
 │
 ├── data/
 │     ├── model/             # 数据模型（消息、好友、系统消息等）
 │     ├── repository/        # 数据仓库（MessageRepo / FriendRepo / SystemRepo）
 │     └── user/              # 登录态管理
 │
 ├── feature/
 │     ├── message/           # 消息列表页（混排、置顶、搜索、埋点、弱网模拟）
 │     ├── chat/              # 聊天页（文本/图片/运营类消息）
 │     ├── dashboard/         # 数据看板（图表、趋势分析、行为分析）
 │     ├── search/            # 消息搜索（关键词高亮、跳转）
 │     ├── weather/           # 全国天气（进阶示例）
 │     ├── login/             # 登录注册
 │     └── mine/              # 我的页面（数据看板入口）
 │
 ├── widget/                  # 自定义控件、自定义弹窗
 ├── assets/                  # 本地 JSON 数据模拟消息
 └── ...
```
## ✨ 核心功能 (Features)

本项目模拟抖音消息体系，在消息混排、好友会话、备注管理、搜索体验、弱网处理等方面进行了实现，并通过数据埋点构建了行为洞察的数据看板。
下面按功能模块逐一介绍。
### 📨 消息列表页

- 好友消息 + 系统消息混排列表（支持不同类型的气泡展示）
- 展示：头像、昵称 / 备注、摘要、时间文案、未读角标
- 点击进入会话
- 长按弹窗操作：置顶、取消置顶、不显示、删除、设置备注
- 下拉刷新（支持弱网模拟）
- 上滑加载更多（模拟分页）
- 支持系统消息插入（如互动提醒）
- 根据好友备注动态刷新 UI
### 💬 聊天页面

- 支持三类消息：
  - 纯文本消息
  - 图片消息
  - 运营类消息（带按钮 CTA）
- 自动根据消息类型切换不同布局（左/右气泡）
- 自动滚动到底部
- 输入框伸缩
- 支持时间分段标题（例如 “昨天”、“星期一”）
### 👤 好友备注

- 长按任意消息进入备注页
- 可编辑好友备注（实时写入 SQLite）
- 冷启动后自动恢复备注
- 若备注为空，展示原始昵称
### 🔍 消息搜索

- 搜索范围：
  - 好友昵称 / 备注
- 支持模糊搜索、高亮命中关键词
- 搜索结果列表复用消息列表 UI
- 点击进入消息详情页（展示关键词附近消息）
### 📡 弱网体验

- 首次加载随机触发弱网（概率可调整）
- Loading → Error → Retry 
- 点击“重试”重新加载
### 🔁 动态消息中心

- 每 5 秒自动生成一条新消息
- 自动写入 SQLite
- 列表自动刷新并滚动到顶部
- 未读数实时更新
- 支持好友消息 / 系统消息
### 📊 数据看板（Dashboard）

- 埋点构建用户行为数据
- 数据分模块展示
- 三页卡片结构：
  1. 原始数据（曝光数、进入次数、点击次数…）
  2. 计算后的行为指标（CTR、停留意愿评分…）
  3. 图表展示（MPAndroidChart）
- 可视化图包括：
  - 饼图
  - 折线图
  - 柱状图
- 动态更新（调用 MetricsAnalyzer）
## ⚙️ 技术栈（Technical Stack）

本项目的技术栈基于现代 Android 客户端开发体系，由架构层、数据层、UI 层、系统层、网络层与工具层组成，覆盖 IM、搜索、埋点与可视化等完整场景。

---

## 1. 架构层（Architecture Layer）

### **1.1 MVVM（Model–View–ViewModel）**
- 使用 ViewModel 管理状态与业务逻辑。
- View 与数据解耦，通过 LiveData/Observer 通信。
- Repository 统一提供数据访问。

### **1.2 Repository Pattern**
- 所有业务数据（消息、好友、系统消息、搜索、天气、埋点）均通过 Repository 提供。
- 完成对 SQLite、本地 JSON 与网络的抽象封装。

### **1.3 模块化 Feature 分层**
- `feature/message`, `feature/chat`, `feature/search`, `feature/dashboard`, etc.
- 按业务领域进行拆分。

---

## 2. 数据层（Data Layer）

### **2.1 SQLite 本地数据库**
- 采用 SQLite 存储核心数据：消息、好友、系统消息、埋点、用户信息。
- 使用文本/整型字段并配合索引排序。
- 使用 LIMIT/OFFSET、LIKE 实现分页与搜索。

### **2.2 数据模型化**
- 使用如 MessageModel、FriendModel、SystemMessageModel 等结构化数据模型进行序列化与业务表达。

### **2.3 数据源融合策略**
- 首次加载使用 assets JSON 初始化。
- 日常使用 SQLite 持久化所有动态数据。

---

## 3. UI 层（Presentation Layer）

### **3.1 RecyclerView 多类型列表**
- 展示消息、搜索结果、天气、仪表盘等。
- 使用 `getItemViewType()` 支持多布局切换。


### **3.2 自定义弹窗组件**
- 包含备注、会话操作、头像选择等场景。

---

## 4. 系统层（System）

### **4.1 Handler 定时消息调度**
- 模拟消息中心周期性推送消息。

### **4.2 生命周期驱动逻辑**
- 页面停留时长、弱网加载、埋点记录基于生命周期控制。

### **4.3 行为埋点系统（MetricsCenter）**
- 记录曝光、进入、停留、点击等行为事件。
- MetricsAnalyzer 用于数据聚合与指标计算。

---

## 5. 网络层（Networking）

### **5.1 Retrofit**
- 天气模块使用 Retrofit + Gson 进行网络请求。
- Repository 封装网络到 UI 的状态转换。

---

## 6. 工具（Utility）

### **6.1 TimeUtils**
- 统一处理消息时间格式与聊天时间切片。

### **6.2 HighlightHelper**
- 搜索关键词高亮。

### **6.3 AvatarUtils**
- 本地头像加载与随机头像生成。

---

## 📦 3. 数据库设计与迁移方案（SQLite Schema & Migration）

本项目的消息体系与埋点体系均基于 SQLite，本地数据库承担了会话列表、聊天记录、用户信息、系统消息、埋点数据的全部持久化职责。

---

### 3.1 数据表结构（Schema Overview）

数据库包含以下核心表：

| 表名 | 作用 |
|------|------|
| `messages` | 好友会话的聊天消息（文本 / 图片 / 运营卡片） |
| `system_messages` | 系统侧推送的消息（互动提醒等） |
| `friends` | 好友信息（昵称、备注、头像、未读数、置顶标记） |
| `user` | 登录状态信息 |
| `metrics` | 埋点事件（页面曝光、点击、停留时长等） |

---

### 3.2 各表字段结构

#### **messages**

| 字段 | 类型 | 说明 |
|------|--------|--------|
| id | INTEGER PRIMARY KEY | 自增主键 |
| sessionId | TEXT | 会话唯一标识（friend_xxx） |
| senderId | INTEGER | 发送者 |
| type | INTEGER | 消息类型（文本/图片/运营卡片/时间条） |
| content | TEXT | 消息内容 |
| imageUrl | TEXT | 图片地址（可为空） |
| timestamp | LONG | 发送时间戳 |
| showTime | INTEGER | 是否为“时间提示”消息 |

索引：  
- `CREATE INDEX idx_messages_session ON messages(sessionId, timestamp DESC);`

---

#### **system_messages**

| 字段 | 类型 | 说明 |
|------|--------|--------|
| id | INTEGER PRIMARY KEY |
| type | INTEGER | 系统消息类型 |
| content | TEXT | 消息内容 |
| extra | TEXT | 附加数据（JSON） |
| timestamp | LONG | 时间戳 |

---

#### **friends**

| 字段 | 类型 | 说明 |
|------|--------|--------|
| id | INTEGER PRIMARY KEY |
| nickname | TEXT |
| remark | TEXT | 好友备注 |
| avatar | TEXT |
| unread | INTEGER | 未读数 |
| isPinned | INTEGER | 是否置顶（0/1） |
| isHidden | INTEGER | 是否隐藏（0/1） |

---

#### **metrics**

| 字段 | 类型 | 说明 |
|------|--------|--------|
| id | INTEGER PRIMARY KEY |
| module | TEXT | 所属功能模块 |
| event | TEXT | 事件类型 |
| value | TEXT | 扩展数值（JSON） |
| timestamp | LONG | 事件时间戳 |

---

### 3.3 Schema 设计特点

- 所有消息类表统一采用 `timestamp` 驱动排序。  
- 会话设计围绕 `sessionId` 组织，会话列表与聊天记录共用该字段。  
- 未读数不依赖消息表动态计算，而直接存入 friends 表，提升读取性能。  
- 埋点系统采用极轻量的 key-value 类型 schema，便于扩展。

---

### 3.4 SQLite Migration 方案

项目内置了一次示例迁移方案：**为 friends 表增加 isPinned 字段**。

迁移步骤如下：

```sql
/**
     * 数据库版本号：
     * v6：新增 system_message 表
     * v7：message 增加 msgType
     * v8：friend 表增加 isPinned / isHidden
     */
-- 1. 创建临时表
CREATE TABLE friends_temp(
    id INTEGER PRIMARY KEY,
    nickname TEXT,
    remark TEXT,
    avatar TEXT,
    unread INTEGER,
    isPinned INTEGER DEFAULT 0,
    isHidden INTEGER DEFAULT 0
);

-- 2. 拷贝旧数据
INSERT INTO friends_temp(id, nickname, remark, avatar, unread, isPinned, isHidden)
SELECT id, nickname, remark, avatar, unread, 0, 0 FROM friends;

-- 3. 删除旧表
DROP TABLE friends;

-- 4. 重命名新表
ALTER TABLE friends_temp RENAME TO friends;
```
## 🛠 遇到的问题与解决方案（Key Issues & Solutions）

### 1. 会话列表出现遗漏或排序异常

**问题表现**  
会话列表中部分会话未显示、排序位置错误、置顶会话未置顶或混排错乱。

**产生原因**  
- `buildSessionList()` 在构建会话列表时，没有同时处理 **隐藏标记（isHidden）过滤** 与 **置顶标记（isPinned）分组**。  
- 系统消息在插入时使用 `list.add(0, ...)`，但未考虑置顶会话排序顺序，导致系统消息可能出现在错误位置。   
- 列表最终排序仅按 `timestamp` 排序，未加入 `isPinned` 的排序权重，造成置顶会话仍按时间排序。

**解决方案**  
- 在构建会话列表时按顺序执行：过滤隐藏 → 补全好友信息 → 插入系统消息 → 分成置顶与普通两个列表 → 分别按时间排序 → 合并。  
- 排序逻辑使用 `(isPinned DESC, timestamp DESC)` 明确写入排序规则。  

---

### 2. 搜索系统跨多表查询耦合严重

**问题表现**  
搜索结果头像缺失、昵称不准确、跳转会话定位异常等问题时常出现。

**产生原因**  
- 搜索数据来自 `messages` 表，但 UI 必须同时显示 `friends` 表内的昵称、备注、头像。  
- 搜索结果如果只按 message 查询，无法补全 Friend 信息；如果补全方式在 Adapter 内执行，会因异步或多次绑定造成数据不一致。  
- 搜索结果需要按 sessionId 分组，但 message 表本身不具备会话维度信息，需要手动构建。

**解决方案**  
- 在 Repository 中统一完成：message 查询 → 按 sessionId 聚合 → 补全 friend 信息 → 生成 SearchResult 模型。  
- UI 只渲染 Repository 产出的结构化数据。  
- 使用统一的数据模型（SearchConversationResult / SearchMessageResult）承载搜索结果。



---

### 3. 未读状态更新错乱（数据库与 UI 不一致）

**问题表现**  
进入聊天页未读未清零、收到消息时未读数错误、搜索返回后 unread 变为异常值。

**产生原因**  
- 未读数的计算依赖当前 UI 列表，而不是数据库中的总消息量。  
- ChatActivity 打开时仅更新 UI，未将 unread 持久化为 0。  
- MessageCenter 新增消息后只更新 messages 表，但未同步更新 friends 表中的 unread 字段。  
- 多处调用 `notifyXXX` 导致 UI 的 unread 与数据库 unread 不一致。

**解决方案**  
- 将 unread 逻辑全部放到 FriendRepository：任何新增消息、进入会话、搜索跳转均需修改数据库中的 unread 字段。  
- ChatActivity 打开会话时立即执行 `friendRepo.clearUnread(friendId)`。  
- 系统消息插入时统一更新 unread 并通知 MessageFragment 重构列表。


---

### 4. 聊天页时间戳错乱与时间提示重复插入

**问题表现**  
出现 1970 时间、重复时间提示、多次刷新后时间段大量堆叠。

**产生原因**  
- 数据库中部分消息的 time 字段为空或为 0，被解析为 Unix 时间戳 0。  
- 填充timestamp的函数将已有的timestamp再次作为“普通消息”参与时间判断，导致重复插入。  

**解决方案**  
- time 字段为 0 的消息直接跳过时间提示计算。  
- 在插入时间提示前过滤掉 TYPE_TIME 的旧提示项，保证只对真实消息计算。  
- 重写 fillTimestamp，使其对同一输入始终生成一致的列表。

---

### 5. 聊天页分页导致滚动跳动或位置重置

**问题表现**  
加载更多旧消息时页面跳回顶部或底部，阅读位置丢失。

**产生原因**  
- LiveData 刷新消息列表后默认触发 scrollToBottom，使分页加载时位置被强制重置。  
- 在 RecyclerView 顶部插入数据后，系统会重新计算布局，使现有 item 的位置发生偏移。  
- 未记录分页加载前的 anchor 位置与像素偏移。

**解决方案**  
- 加载更多前记录 firstVisiblePosition 与 firstItemTopOffset。  
- 插入新数据后使用 scrollToPositionWithOffset() 精准恢复位置。  
- 在分页场景下禁用自动滚动到底部，只在“发送消息”操作时启用。

---

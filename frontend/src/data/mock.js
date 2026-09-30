// 高中数学AI诊断测评系统 - Mock数据

// 用户信息
export const userInfo = {
  name: '张同学',
  grade: '高二',
  avatar: '',
  targetScore: 110,
  totalQuestionTypes: 103,
  masteredTypes: 64,
  testCount: 12,
  studyDays: 45
}

// 知识模块掌握数据
export const knowledgeModules = [
  { id: 1, name: '函数', icon: 'f(x)', color: '#4A7BF7', percent: 72, mastered: 18, needFix: 6, pending: 3, untested: 8, na: 3 },
  { id: 2, name: '三角函数', icon: '△', color: '#7B61FF', percent: 56, mastered: 15, needFix: 10, pending: 5, untested: 10, na: 2 },
  { id: 3, name: '数列', icon: 'aₙ', color: '#34C759', percent: 80, mastered: 20, needFix: 3, pending: 2, untested: 5, na: 2 },
  { id: 4, name: '导数', icon: "f'", color: '#FF9500', percent: 68, mastered: 17, needFix: 5, pending: 3, untested: 6, na: 2 },
  { id: 5, name: '圆锥曲线', icon: '⊖', color: '#4A7BF7', percent: 41, mastered: 11, needFix: 9, pending: 3, untested: 12, na: 2 },
  { id: 6, name: '立体几何', icon: '⬡', color: '#34C759', percent: 0, mastered: 0, needFix: 0, pending: 0, untested: 0, na: 25, status: 'na' }
]

// 测评记录
export const testRecords = [
  {
    id: 1,
    title: '导数章节测试',
    date: '2026.08.10',
    type: 'chapter',
    score: 68,
    total: 100,
    conclusion: '基础尚可，恒成立与极值点偏移建议重点加强',
    mastered: 68,
    total_types: 100,
    unmastered: 32
  },
  {
    id: 2,
    title: '函数功底测评',
    date: '2026.08.05',
    type: 'foundation',
    score: 72,
    total: 100,
    conclusion: '函数基础较好，图像变换需加强',
    mastered: 72,
    total_types: 100,
    unmastered: 28
  },
  {
    id: 3,
    title: '三角函数专题测试',
    date: '2026.07.28',
    type: 'topic',
    score: 56,
    total: 100,
    conclusion: '三角恒等变换薄弱，建议系统复习',
    mastered: 56,
    total_types: 100,
    unmastered: 44
  }
]

// 学习建议
export const studySuggestions = [
  {
    id: 1,
    type: 'priority',
    title: '导数恒成立问题',
    desc: '上次测评中发现明显问题',
    tag: '优先补',
    tagColor: '#FF9500'
  },
  {
    id: 2,
    type: 'practice',
    title: '函数单调性参数问题',
    desc: '基础已经掌握，可以继续加强',
    tag: '建议练',
    tagColor: '#34C759'
  }
]

// 推荐资料
export const recommendedMaterials = [
  {
    id: 1,
    title: '导数恒成立专题讲解',
    type: '讲解类',
    typeTag: '专题突破',
    desc: '适合：恒成立基础薄弱，自学使用',
    tags: ['恒成立', '导数', '参数'],
    price: 19.9,
    originalPrice: 29.9,
    matchRate: 96
  },
  {
    id: 2,
    title: '导数章节系统训练',
    type: '刷题类',
    typeTag: '章节体系',
    desc: '适合：系统巩固导数题型',
    tags: ['导数', '刷题', '系统训练'],
    price: 29.9,
    originalPrice: 39.9,
    matchRate: 93
  }
]

// 资料库数据
export const learningStages = [
  { id: 'sync', name: '同步学习', desc: '教材同步·夯实基础', icon: '📘' },
  { id: 'review', name: '高三复习', desc: '考点整合·系统提升', icon: '🎯' },
  { id: 'summer', name: '暑假资料', desc: '预习衔接·提前起跑', icon: '️' },
  { id: 'winter', name: '寒假资料', desc: '查漏补缺·巩固提高', icon: '❄️' }
]

export const textbookVersions = ['人教A版', '人教B版', '北师大版', '苏教版', '湘教版', '其他版本']
export const textbookVolumes = ['必修一', '必修二', '选择性必修一', '选择性必修二', '选择性必修三']
export const chapters = ['全部', '集合与常用逻辑用语', '函数', '立体几何初步', '平面向量', '三角函数', '数列', '导数', '不等式', '概率统计']
export const materialTypes = ['全部', '讲解类', '刷题类', '功能类']
export const materialPurposes = ['全部', '章节体系', '专题突破']

export const materials = [
  {
    id: 1,
    title: '函数章节系统讲解（必修一）',
    type: '讲解类',
    purpose: '章节体系',
    desc: '紧贴教材，逐节讲解函数的定义、性质与图像，例题丰富，适合同步学习。',
    audience: '高一同步学习，基础中等及以下',
    tags: ['函数', '定义域', '值域', '图像性质'],
    price: 15.9,
    matchRate: 96
  },
  {
    id: 2,
    title: '函数章节同步训练（必修一）',
    type: '刷题类',
    purpose: '章节体系',
    desc: '精选同步习题，覆盖教材例题与典型题，巩固基础，提升解题能力。',
    audience: '高一同步刷题，基础巩固',
    tags: ['函数', '基础题', '中等题', '应用题'],
    price: 12.9,
    matchRate: 92
  },
  {
    id: 3,
    title: '函数图像与性质专题突破',
    type: '讲解类',
    purpose: '专题突破',
    desc: '深度解析函数图像变换、单调性、奇偶性等核心考点，方法总结到位。',
    audience: '成绩中等及以上，突破难点',
    tags: ['图像变换', '单调性', '奇偶性', '综合应用'],
    price: 16.9,
    matchRate: 91
  },
  {
    id: 4,
    title: '高中数学二级结论大全',
    type: '功能类',
    purpose: '工具资料',
    desc: '系统整理高中数学常用二级结论，快速查阅，解题更高效。',
    audience: '所有高中生',
    tags: ['二级结论', '快速查阅', '解题技巧'],
    price: 9.9,
    matchRate: 89
  },
  {
    id: 5,
    title: '常用公式速查手册',
    type: '功能类',
    purpose: '工具资料',
    desc: '按章节知识点分类，收录常用公式与定理，便于记忆与查找。',
    audience: '所有高中生',
    tags: ['公式', '定理', '快速查找'],
    price: 6.9,
    matchRate: 87
  },
  {
    id: 6,
    title: '导数专题突破精讲',
    type: '讲解类',
    purpose: '专题突破',
    desc: '系统梳理导数核心考点与典型题型，方法技巧全面总结。',
    audience: '中等及以上，冲刺高分',
    tags: ['导数', '极值', '最值', '恒成立'],
    price: 19.9,
    matchRate: 96
  },
  {
    id: 7,
    title: '圆锥曲线综合突破100题',
    type: '刷题类',
    purpose: '专题突破',
    desc: '精选高考真题与模拟题，突破圆锥曲线压轴题。',
    audience: '中等及以上，突破瓶颈',
    tags: ['椭圆', '抛物线', '双曲线', '综合题'],
    price: 16.9,
    matchRate: 93
  }
]

// 测评题目数据
export const testQuestions = [
  {
    id: 1,
    module: '导数',
    knowledgePoint: '导数的几何意义',
    questionType: '单选题',
    difficulty: 'L2',
    question: '已知函数 f(x) = x³ - 3x + 1，则 f(x) 在 x = 1 处的切线斜率为？',
    options: ['A. 0', 'B. 1', 'C. 2', 'D. 3'],
    answer: 0,
    analysis: 'f\'(x) = 3x² - 3，f\'(1) = 3 - 3 = 0'
  },
  {
    id: 2,
    module: '导数',
    knowledgePoint: '函数的单调性',
    questionType: '单选题',
    difficulty: 'L2',
    question: '函数 f(x) = x² - 2x 的单调递增区间是？',
    options: ['A. (-∞, 1)', 'B. (1, +)', 'C. (-∞, 0)', 'D. (0, +∞)'],
    answer: 1,
    analysis: 'f\'(x) = 2x - 2，令 f\'(x) > 0 得 x > 1'
  },
  {
    id: 3,
    module: '导数',
    knowledgePoint: '极值与最值',
    questionType: '单选题',
    difficulty: 'L3',
    question: '函数 f(x) = x³ - 3x 在区间 [-2, 2] 上的最大值为？',
    options: ['A. 2', 'B. 4', 'C. -2', 'D. 0'],
    answer: 0,
    analysis: 'f\'(x) = 3x² - 3 = 0，x = ±1。f(-2) = -2, f(-1) = 2, f(1) = -2, f(2) = 2。最大值为 2'
  },
  {
    id: 4,
    module: '函数',
    knowledgePoint: '函数的定义域',
    questionType: '单选题',
    difficulty: 'L1',
    question: '函数 f(x) = √(x-1) 的定义域是？',
    options: ['A. [1, +∞)', 'B. (1, +∞)', 'C. (-∞, 1]', 'D. R'],
    answer: 0,
    analysis: '根号内非负：x - 1 ≥ 0，即 x ≥ 1'
  },
  {
    id: 5,
    module: '三角函数',
    knowledgePoint: '三角恒等变换',
    questionType: '单选题',
    difficulty: 'L2',
    question: 'sin(2α) = 2sinαcosα 是？',
    options: ['A. 二倍角公式', 'B. 和差化积公式', 'C. 积化和差公式', 'D. 诱导公式'],
    answer: 0,
    analysis: '这是二倍角的正弦公式'
  },
  {
    id: 6,
    module: '数列',
    knowledgePoint: '等差数列',
    questionType: '单选题',
    difficulty: 'L1',
    question: '等差数列 {aₙ} 中，a₁ = 1，d = 2，则 a₅ = ？',
    options: ['A. 7', 'B. 8', 'C. 9', 'D. 10'],
    answer: 2,
    analysis: 'a₅ = a₁ + 4d = 1 + 8 = 9'
  },
  {
    id: 7,
    module: '圆锥曲线',
    knowledgePoint: '椭圆',
    questionType: '单选题',
    difficulty: 'L2',
    question: '椭圆 x²/4 + y²/3 = 1 的焦距为？',
    options: ['A. 1', 'B. 2', 'C. √7', 'D. 4'],
    answer: 1,
    analysis: 'a² = 4, b² = 3, c² = a² - b² = 1, c = 1, 焦距 = 2c = 2'
  },
  {
    id: 8,
    module: '导数',
    knowledgePoint: '恒成立问题',
    questionType: '单选题',
    difficulty: 'L3',
    question: '若不等式 x² - 2ax + 1 ≥ 0 对任意 x ∈ R 恒成立，则 a 的取值范围是？',
    options: ['A. [-1, 1]', 'B. (-1, 1)', 'C. (-∞, -1] ∪ [1, +∞)', 'D. (-∞, -1) ∪ (1, +∞)'],
    answer: 0,
    analysis: '判别式 Δ = 4a² - 4 ≤ 0，解得 -1 ≤ a ≤ 1'
  }
]

// 管理后台 - 知识点数据
export const knowledgePoints = [
  {
    id: 1,
    name: '函数',
    level: 1,
    children: [
      { id: 11, name: '函数的概念与表示', level: 2, questionCount: 15, difficulty: 'L1-L2' },
      { id: 12, name: '函数的单调性', level: 2, questionCount: 20, difficulty: 'L2-L3' },
      { id: 13, name: '函数的奇偶性', level: 2, questionCount: 12, difficulty: 'L2' },
      { id: 14, name: '函数的图像变换', level: 2, questionCount: 18, difficulty: 'L2-L3' }
    ]
  },
  {
    id: 2,
    name: '导数',
    level: 1,
    children: [
      { id: 21, name: '导数的概念与几何意义', level: 2, questionCount: 10, difficulty: 'L1-L2' },
      { id: 22, name: '导数的运算', level: 2, questionCount: 15, difficulty: 'L2' },
      { id: 23, name: '导数与单调性', level: 2, questionCount: 20, difficulty: 'L2-L3' },
      { id: 24, name: '导数与极值最值', level: 2, questionCount: 18, difficulty: 'L3' },
      { id: 25, name: '恒成立问题', level: 2, questionCount: 15, difficulty: 'L3-L4' },
      { id: 26, name: '零点问题', level: 2, questionCount: 12, difficulty: 'L3-L4' }
    ]
  },
  {
    id: 3,
    name: '三角函数',
    level: 1,
    children: [
      { id: 31, name: '任意角与弧度制', level: 2, questionCount: 8, difficulty: 'L1' },
      { id: 32, name: '三角函数定义', level: 2, questionCount: 12, difficulty: 'L1-L2' },
      { id: 33, name: '三角恒等变换', level: 2, questionCount: 20, difficulty: 'L2-L3' },
      { id: 34, name: '三角函数图像与性质', level: 2, questionCount: 15, difficulty: 'L2' }
    ]
  },
  {
    id: 4,
    name: '数列',
    level: 1,
    children: [
      { id: 41, name: '等差数列', level: 2, questionCount: 15, difficulty: 'L1-L2' },
      { id: 42, name: '等比数列', level: 2, questionCount: 15, difficulty: 'L2' },
      { id: 43, name: '数列求和', level: 2, questionCount: 18, difficulty: 'L2-L3' }
    ]
  },
  {
    id: 5,
    name: '圆锥曲线',
    level: 1,
    children: [
      { id: 51, name: '椭圆', level: 2, questionCount: 15, difficulty: 'L2-L3' },
      { id: 52, name: '双曲线', level: 2, questionCount: 12, difficulty: 'L3' },
      { id: 53, name: '抛物线', level: 2, questionCount: 12, difficulty: 'L2-L3' },
      { id: 54, name: '直线与圆锥曲线', level: 2, questionCount: 20, difficulty: 'L3-L4' }
    ]
  }
]

// 管理后台 - 模板数据
export const templates = [
  { id: 1, name: '高三功底测·120+', stage: '高三', scoreRange: '120+', questionCount: 30, duration: 35, status: 'active', updatedAt: '2024-12-01 10:30' },
  { id: 2, name: '高三功底测·100-120', stage: '高三', scoreRange: '100-120', questionCount: 30, duration: 35, status: 'active', updatedAt: '2024-12-01 09:45' },
  { id: 3, name: '高三功底测·70-100', stage: '高三', scoreRange: '70-100', questionCount: 30, duration: 35, status: 'active', updatedAt: '2024-12-01 09:20' },
  { id: 4, name: '高三功底测·70以下', stage: '高三', scoreRange: '70以下', questionCount: 30, duration: 35, status: 'active', updatedAt: '2024-12-01 09:00' }
]

export const diagnosticModules = [
  { id: 1, name: '概率统计', diagnosticPoints: 4, questionPositions: 7, desc: '主要考查条件概率、分布、期望等综合应用' },
  { id: 2, name: '圆锥曲线', diagnosticPoints: 5, questionPositions: 9, desc: '主要考查联立、条件转化、参数处理等' },
  { id: 3, name: '导数', diagnosticPoints: 6, questionPositions: 10, desc: '主要考查单调性、参数讨论、恒成立等' },
  { id: 4, name: '综合能力', diagnosticPoints: 2, questionPositions: 4, desc: '考查新定义、综合建模等能力' }
]

export const diagnosticPoints = [
  { id: 1, name: '单调性基本模型', purpose: '判断学生是否能完成导数单调性的基本判', positions: 2, intensity: '普通', status: 'active' },
  { id: 2, name: '参数分类讨论能力', purpose: '判断学生能否识别参数临界情况并分', positions: 2, intensity: '重点', status: 'active' },
  { id: 3, name: '恒成立中的函数构造', purpose: '判断学生是否会构造导数解决恒成立问题', positions: 2, intensity: '重点', status: 'active' },
  { id: 4, name: '极值问题综合应用', purpose: '判断学生能否综合运用导数解决复杂极值', positions: 2, intensity: '普通', status: 'active' },
  { id: 5, name: '导数与函数综合转化', purpose: '判断学生能否完成导数与函数的综合转化', positions: 2, intensity: '普通', status: 'active' },
  { id: 6, name: '创新题型理解能力', purpose: '判断学生面对创新题型的理解与建模能力', positions: 2, intensity: '普通', status: 'active' }
]

// AI诊断规则
export const diagnosisRules = {
  masteryThreshold: 80,
  fixThreshold: 60,
  confirmThreshold: 40,
  levels: [
    { name: '已掌握', color: '#34C759', min: 80, desc: '多次检测表现稳定，会定期抽查保持状态' },
    { name: '待补齐', color: '#FF9500', min: 60, desc: '多次检测仍有问题，需要加强学习与练习' },
    { name: '待确认', color: '#4A7BF7', min: 40, desc: '只出现过1次且答错，需要再次检测确认' },
    { name: '未检测', color: '#999999', min: 0, desc: '还未覆盖到的题型，需要通过测评进行检测' },
    { name: '暂不适用', color: '#CCCCCC', min: -1, desc: '当前学习阶段暂不纳入检测，可在后续阶段解锁' }
  ]
}

// 管理员统计数据
export const adminStats = {
  totalQuestions: 256,
  totalTests: 1234,
  activeUsers: 89,
  avgAccuracy: 68
}

// 最近测评记录（管理员视角）
export const recentTestRecords = [
  { id: 1, studentName: '张同学', testName: '导数章节测试', score: 68, date: '2026.09.28', status: 'completed' },
  { id: 2, studentName: '李同学', testName: '函数功底测评', score: 82, date: '2026.09.28', status: 'completed' },
  { id: 3, studentName: '王同学', testName: '三角函数专题', score: 45, date: '2026.09.27', status: 'completed' },
  { id: 4, studentName: '赵同学', testName: '数列章节测试', score: 91, date: '2026.09.27', status: 'completed' },
  { id: 5, studentName: '刘同学', testName: '圆锥曲线测评', score: 56, date: '2026.09.26', status: 'completed' }
]

-- 用户数据
INSERT INTO users (phone, password, name, role, grade, target_score) VALUES ('13800138000', '123456', '张三', 'student', '高二', 120);
INSERT INTO users (phone, password, name, role, grade, target_score) VALUES ('admin', 'admin', '管理员', 'admin', NULL, 0);

-- 知识模块（6个）
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('函数', 'f(x)', '#4A7BF7', 1);
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('三角函数', '△', '#FF9500', 2);
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('数列', 'an', '#34C759', 3);
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('导数', 'f''', '#AF52DE', 4);
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('圆锥曲线', '○', '#FF3B30', 5);
INSERT INTO knowledge_modules (name, icon, color, sort_order) VALUES ('立体几何', '□', '#5AC8FA', 6);

-- 知识点（每个模块2-3个）
-- 函数（module_id=1）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('函数的概念', 15, '基础', true, 1, 1);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('单调性', 18, '中等', true, 2, 1);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('奇偶性', 12, '中等', true, 3, 1);

-- 三角函数（module_id=2）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('三角恒等变换', 16, '中等', true, 1, 2);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('正弦定理', 14, '中等', true, 2, 2);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('余弦定理', 13, '中等', true, 3, 2);

-- 数列（module_id=3）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('等差数列', 17, '基础', true, 1, 3);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('等比数列', 15, '中等', true, 2, 3);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('数列求和', 19, '较难', true, 3, 3);

-- 导数（module_id=4）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('导数的几何意义', 11, '基础', true, 1, 4);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('函数的单调性', 20, '中等', true, 2, 4);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('极值与最值', 22, '较难', true, 3, 4);

-- 圆锥曲线（module_id=5）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('椭圆', 18, '中等', true, 1, 5);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('双曲线', 14, '较难', true, 2, 5);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('抛物线', 16, '较难', true, 3, 5);

-- 立体几何（module_id=6）
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('空间几何体', 13, '基础', true, 1, 6);
INSERT INTO knowledge_points (name, question_count, difficulty, enabled, sort_order, module_id) VALUES ('点线面关系', 17, '中等', true, 2, 6);

-- 题目（8道，覆盖各模块）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '基础', '已知函数f(x)=x²-2x+1，则f(2)的值为', '["1","2","3","4"]', 0, 'f(2)=2²-2×2+1=4-4+1=1', 1, 1);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '函数f(x)=x³-3x在区间(-1,1)上的单调性是', '["单调递增","单调递减","先增后减","先减后增"]', 1, 'f''(x)=3x²-3=3(x²-1)，在(-1,1)上f''(x)<0，故单调递减', 1, 2);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '已知sinα=3/5，α为第二象限角，则cosα的值为', '["4/5","-4/5","3/5","-3/5"]', 1, 'α为第二象限角，cosα<0，cosα=-√(1-sin²α)=-√(1-9/25)=-4/5', 2, 4);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '基础', '等差数列{an}中，a1=2，d=3，则a5=', '["11","12","13","14"]', 3, 'a5=a1+4d=2+4×3=2+12=14', 3, 7);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '较难', '函数f(x)=x³-3x²+1的极小值为', '["-3","-1","0","1"]', 0, 'f''(x)=3x²-6x=3x(x-2)，令f''(x)=0得x=0或x=2，f''(x)在x=2处由负变正，f(2)=8-12+1=-3', 4, 12);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '椭圆x²/4+y²=1的焦距为', '["2","2√3","4","√3"]', 1, 'a²=4,b²=1,c²=a²-b²=3,c=√3，焦距=2c=2√3', 5, 13);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '基础', '正方体ABCD-A1B1C1D1中，直线AA1与平面ABCD的位置关系是', '["平行","垂直","相交但不垂直","在平面内"]', 1, 'AA1是正方体的棱，垂直于底面ABCD', 6, 16);

INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '较难', '等比数列{an}中，a1=1，a4=8，则公比q=', '["2","-2","2或-2","4"]', 0, 'a4=a1×q³，8=1×q³，q³=8，q=2', 3, 8);

-- 资料（7份）
INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('函数章节系统讲解', '讲解类', '章节体系', '同步学习', '人教A版', '必修一', '第一章', '系统讲解函数的概念、性质、图像，适合高一同步学习', '高一学生', '函数,基础,系统', 99.00, 199.00, 95);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('三角函数专题突破', '刷题类', '专题突破', '高三复习', '人教A版', '全一册', '三角函数', '精选三角函数高考真题和模拟题，专项突破', '高三学生', '三角函数,专题,高考', 79.00, 159.00, 88);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('数列刷题宝典', '刷题类', '章节体系', '同步学习', '人教A版', '必修五', '数列', '等差等比数列基础题+提高题，循序渐进', '高二学生', '数列,刷题,基础', 69.00, 139.00, 92);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('导数压轴题解析', '讲解类', '专题突破', '高三复习', '人教A版', '全一册', '导数', '精选导数压轴题，详细解析解题思路和方法', '高三尖子生', '导数,压轴,高考', 129.00, 259.00, 85);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('圆锥曲线工具手册', '功能类', '工具资料', '高三复习', '人教A版', '全一册', '圆锥曲线', '圆锥曲线公式速查+典型例题，考前必备', '高三学生', '圆锥曲线,工具,速查', 49.00, 99.00, 90);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('立体几何思维导图', '功能类', '工具资料', '同步学习', '人教A版', '必修二', '立体几何', '立体几何知识框架图+典型模型，快速建立空间想象', '高二学生', '立体几何,思维导图,模型', 39.00, 79.00, 87);

INSERT INTO materials (title, type, purpose, stage, version, volume, chapter, description, audience, tags, price, original_price, match_rate) VALUES
('高考真题汇编', '刷题类', '专题突破', '高三复习', '人教A版', '全一册', '综合', '近5年高考数学真题汇编，含详细解析', '高三学生', '高考,真题,汇编', 89.00, 179.00, 93);

-- 模板（4个）
INSERT INTO templates (name, stage, score_range, question_count, duration, status) VALUES ('功底测评', '全模块', '0-100', 8, 30, 'active');
INSERT INTO templates (name, stage, score_range, question_count, duration, status) VALUES ('章节测试', '单模块', '0-100', 10, 40, 'active');
INSERT INTO templates (name, stage, score_range, question_count, duration, status) VALUES ('专题突破', '重难点', '0-100', 12, 50, 'active');
INSERT INTO templates (name, stage, score_range, question_count, duration, status) VALUES ('期末模拟', '综合', '0-150', 20, 90, 'active');

-- 诊断模块（4个）
INSERT INTO diagnostic_modules (name, diagnostic_points_count, question_positions, description) VALUES ('知识掌握诊断', 3, 15, '诊断学生对各知识点的掌握程度，找出薄弱环节');
INSERT INTO diagnostic_modules (name, diagnostic_points_count, question_positions, description) VALUES ('解题能力诊断', 2, 12, '诊断学生的解题方法和技巧，评估解题效率');
INSERT INTO diagnostic_modules (name, diagnostic_points_count, question_positions, description) VALUES ('思维方法诊断', 2, 10, '诊断学生的数学思维方法，如分类讨论、数形结合等');
INSERT INTO diagnostic_modules (name, diagnostic_points_count, question_positions, description) VALUES ('应试技巧诊断', 2, 8, '诊断学生的应试技巧，如时间分配、审题能力等');

-- 诊断点（每个模块2个）
-- 知识掌握诊断（module_id=1）
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('基础概念掌握度', '检测学生对基础概念的理解程度', 5, '普通', 'active', 1);
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('公式运用能力', '检测学生运用公式解题的能力', 5, '普通', 'active', 1);
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('综合应用能力', '检测学生综合运用知识的能力', 5, '较强', 'active', 1);

-- 解题能力诊断（module_id=2）
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('解题方法选择', '检测学生选择合适解题方法的能力', 6, '普通', 'active', 2);
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('计算准确性', '检测学生计算的准确性和效率', 6, '普通', 'active', 2);

-- 思维方法诊断（module_id=3）
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('分类讨论意识', '检测学生分类讨论的思维意识', 5, '较强', 'active', 3);
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('数形结合能力', '检测学生运用图形辅助解题的能力', 5, '普通', 'active', 3);

-- 应试技巧诊断（module_id=4）
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('审题能力', '检测学生审题的准确性和完整性', 4, '普通', 'active', 4);
INSERT INTO diagnostic_points (name, purpose, positions, intensity, status, module_id) VALUES ('时间分配策略', '检测学生合理分配答题时间的能力', 4, '普通', 'active', 4);

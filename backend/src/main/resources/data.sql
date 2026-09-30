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

-- 主观题（4道，覆盖核心模块）
INSERT INTO questions (question_type, difficulty, content, options, answer, reference_answer, analysis, module_id, knowledge_point_id) VALUES
('主观题', '中等', '已知函数f(x)=x³-3x²+2，求：(1)f(x)的单调区间；(2)f(x)的极值。', NULL, NULL, 
'(1)单调递增区间：(-∞,0)和(2,+∞)；单调递减区间：(0,2)。(2)极大值f(0)=2，极小值f(2)=-2。',
'f''(x)=3x²-6x=3x(x-2)，令f''(x)=0得x=0或x=2。当x<0或x>2时f''(x)>0，函数递增；当0<x<2时f''(x)<0，函数递减。', 4, 12);

INSERT INTO questions (question_type, difficulty, content, options, answer, reference_answer, analysis, module_id, knowledge_point_id) VALUES
('主观题', '中等', '在△ABC中，已知a=5，b=7，A=30°，求sinB和边c的值。', NULL, NULL,
'sinB=7/10，c=√39或c=5√3。',
'由正弦定理a/sinA=b/sinB，得sinB=bsinA/a=7×(1/2)/5=7/10。由余弦定理a²=b²+c²-2bccosA，代入得25=49+c²-7√3c，解得c=√39或c=5√3。', 2, 5);

INSERT INTO questions (question_type, difficulty, content, options, answer, reference_answer, analysis, module_id, knowledge_point_id) VALUES
('主观题', '较难', '已知等差数列{an}的前n项和为Sn，a3=7，S4=24，求：(1)通项公式an；(2)前n项和Sn。', NULL, NULL,
'(1)an=2n+1；(2)Sn=n²+2n。',
'设首项为a1，公差为d。由a3=a1+2d=7，S4=4a1+6d=24，解得a1=3，d=2。所以an=a1+(n-1)d=3+2(n-1)=2n+1，Sn=na1+n(n-1)d/2=3n+n(n-1)=n²+2n。', 3, 7);

INSERT INTO questions (question_type, difficulty, content, options, answer, reference_answer, analysis, module_id, knowledge_point_id) VALUES
('主观题', '基础', '证明：函数f(x)=x²在[0,+∞)上是单调递增函数。', NULL, NULL,
'证明：任取x1,x2∈[0,+∞)，设x1<x2，则f(x1)-f(x2)=x1²-x2²=(x1+x2)(x1-x2)。因为x1,x2≥0且x1<x2，所以x1+x2>0，x1-x2<0，故f(x1)-f(x2)<0，即f(x1)<f(x2)，所以f(x)在[0,+∞)上单调递增。',
'使用单调性定义证明，任取两点x1<x2，计算f(x1)-f(x2)并判断符号。', 1, 2);

-- 解三角形专题题目（11道）
-- 题目1：正弦定理中R的含义（基础）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '基础', '正弦定理中R是指的是什么？', '["三角形内切圆半径","三角形外接圆半径","一个没有意义的常数","不知道"]', 1, 
'正弦定理a/sinA=b/sinB=c/sinC=2R中，R是三角形外接圆的半径。', 2, 5);

-- 题目2：三角恒等变换（中等）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', 'sin(2α)=sin(2β)能否得到α=β？', '["可以","不一定","一定不会","本题不会"]', 1, 
'sin(2α)=sin(2β)只能得到2α=2β+2kπ或2α=π-2β+2kπ，即α=β+kπ或α=π/2-β+kπ，所以不一定能推出α=β。', 2, 4);

-- 题目3：边角互化理解（中等）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', 'acosC+√3asinC-b-c=0这道题左边所有的边是否可以直接化为对应的sin值？', '["可以","不可以","不会"]', 0, 
'根据正弦定理a=2RsinA, b=2RsinB, c=2RsinC，可以将边化为对应的正弦值，这是解三角形中常用的边角互化方法。', 2, 5);

-- 题目4：海伦公式（较难）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '较难', '以下哪个是海伦公式？', '["S=√[p(p-a)(p-b)(p-c)]","S=√[p(a-p)(p-b)(p-c)]","S=√[p(a-p)(b-p)(c-p)]"]', 0, 
'海伦公式：S=√[p(p-a)(p-b)(p-c)]，其中p=(a+b+c)/2是半周长。这是三角形面积公式的拓展，适用于已知三边求面积的情况。', 2, 5);

-- 题目5：边角互化（中等）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '在三角形中，已知a:b:c，能否一定能够推出sinA:sinB:sinC的值，也是否一定能够推出A:B:C的比值？', '["可以；不可以","可以；可以","不可以；可以","不可以；不可以"]', 0, 
'由正弦定理a/sinA=b/sinB=c/sinC=2R，可得a:b:c=sinA:sinB:sinC，所以可以推出正弦比。但边的比不能直接推出角的比，因为正弦函数不是线性函数。', 2, 5);

-- 题目6：角平分线定理（中等）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '在三角形中，以下哪个是角平分线定理？AD是∠BAC的平分线，则', '["AB/AC=BD/CD","AB·CD=BD·AC","AC/CD=BD/CD"]', 0, 
'角平分线定理：三角形一个角的平分线分对边所成的两条线段与这个角的两邻边对应成比例，即AB/AC=BD/CD。这是高考中常见的重要方法。', 2, 5);

-- 题目7：角平分线处理方法（较难，多选）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('多选题', '较难', '解三角形中涉及角平分线时，请勾选出所有能够处理方法', '["角平分线定理：AB/AC=BD/CD","等面积法：S△ABC=S△ABD+S△ACD","张角定理(中线形式)","双余弦贴贴法：cos∠ADB+cos∠ADC=0"]', 15, 
'角平分线的处理方法包括：(1)角平分线定理；(2)等面积法；(3)张角定理；(4)双余弦法（利用补角关系cos∠ADB+cos∠ADC=0）。这些都是处理角平分线问题的有效方法。', 2, 5);

-- 题目8：高线性质（较难）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '较难', '以下几个等式哪个是正确的？（h1,h2,h3为三角形三边上的高）', '["h1:h2:h3=1/a:1/b:1/c=1/sinA:1/sinB:1/sinC","h1:h2:h3=1/a:1/b:1/c=1/cosA:1/cosB:1/cosC","h1:h2:h3=1/a:1/b:1/c=1/sinA:1/cosB:1/cosC","不会做"]', 0, 
'由面积公式S=ah1/2=bh2/2=ch3/2，得h1=2S/a, h2=2S/b, h3=2S/c，所以h1:h2:h3=1/a:1/b:1/c。再由正弦定理a=2RsinA等，得1/a:1/b:1/c=1/sinA:1/sinB:1/sinC。', 2, 5);

-- 题目9：范围问题基本技巧（中等，多选）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('多选题', '中等', '最常见的处理范围问题的手段有哪两种？', '["基本不等式","二次函数值域","化为三角函数值域问题","求导法"]', 7, 
'解三角形中处理范围问题的常用方法：(1)基本不等式；(2)化为三角函数值域问题。这两种是最常见的方法，二次函数值域和求导法在特定情况下也会用到。', 2, 5);

-- 题目10：锐角三角形范围（中等）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('单选题', '中等', '求解解三角形范围问题时，若A=π/3，△ABC是锐角三角形，B的取值范围是多少？', '["π/6<B<π/2","π/3<B<π/2","π/4<B<π/2"]', 1, 
'因为A=π/3，所以B+C=2π/3。锐角三角形要求A,B,C都小于π/2。由C=2π/3-B<π/2得B>π/6；由B<π/2得B<π/2。但还需C>0，即B<2π/3。综合得π/3<B<π/2（因为B>A=π/3时才能构成锐角三角形）。', 2, 5);

-- 题目11：解三角形综合（较难，主观题）
INSERT INTO questions (question_type, difficulty, content, options, answer, reference_answer, analysis, module_id, knowledge_point_id) VALUES
('主观题', '较难', '记△ABC的内角A，B，C的对边分别为a，b，c，已知tanA=(sinC+sinB)/(cosC+cosB)。(1)求A的值；(2)若△ABC是锐角三角形，求(b²-bc)/a²的取值范围。', 
NULL, NULL, 
'(1)A=π/3；(2)-1/3≤(b²-bc)/a²<2/3。',
'(1)由tanA=(sinC+sinB)/(cosC+cosB)，利用和差化积公式，右边=sin(B+C)/cos(B+C)的相反数（需推导），最终得A=π/3。(2)由正弦定理和锐角三角形条件，将(b²-bc)/a²化为关于B的三角函数，利用B的范围求得值域。', 2, 5);

-- 题目12：解三角形综合判断（较难，多选）
INSERT INTO questions (question_type, difficulty, content, options, answer, analysis, module_id, knowledge_point_id) VALUES
('多选题', '较难', '[长沙望城一中2025高一期末]在△ABC中，角A，B，C的对边分别是a，b，c，下列说法正确的是', 
'["若A>B，则cosA<cosB","若A=30°，b=5，a=2，则△ABC有两解","若(a-c·cosB)=a·cosC，则△ABC为等腰三角形或直角三角形","若cosAcosBcosC>0，则△ABC为钝角三角形"]', 11, 
'A正确：在三角形中A>B等价于a>b等价于sinA>sinB，由于cos在(0,π)上递减，所以cosA<cosB。B正确：由正弦定理sinB=bsinA/a=5/4>1无解，实际上只有一解或无解，需验证。C正确：利用正弦定理化简可得。D正确：cosAcosBcosC>0说明三个余弦值同号，在三角形中只能都为正（锐角三角形）或两负一正（不可能），实际应为锐角三角形。', 2, 5);

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

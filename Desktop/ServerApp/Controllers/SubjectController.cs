using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using DbModel;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ServerApp
{

    public class SubjectController : Controller
    {
        #region Protected Members

        /// <summary>
        /// Database của server
        /// </summary>
        protected ApplicationDbContext mContext { set; get; }

        /// <summary>
        /// The manager for handling user creation, deletion, searching, roles etc...
        /// </summary>
        protected UserManager<ApplicationUser> mUserManager;

        /// <summary>
        /// The manager for handling signing in and out for our users
        /// </summary>
        protected SignInManager<ApplicationUser> mSignInManager;

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        /// <param name="context">The injected context</param>
        /// <param name="signInManager">The Identity sign in manager</param>
        /// <param name="userManager">The Identity user manager</param>
        public SubjectController(ApplicationDbContext context,
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager)
        {
            mContext = context;
            mUserManager = userManager;
            mSignInManager = signInManager;
        }

        #endregion

        /// <summary>
        /// Gửi yêu cầu tạo môn học mới, bao gồm cả lịch học
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        [AuthorizeToken]
        [Route("api/subject")]
        [HttpPost]
        public ApiResponse<CreateSubjectResultApiModel> PostSubject([FromBody] CreateSubjectCredentialsDataModel data)
        {
            try
            {
                mContext.Subject.Add(data.Subject);
                mContext.SaveChanges();
            }
            catch (Exception ex)
            {
                return new ApiResponse<CreateSubjectResultApiModel>
                {
                    ErrorMessage = "Tạo môn học thất bại." +
                        "\nCó thể mã học phần bị trùng hoặc chưa điền đầy đủ các thông tin cần thiết.",
                };
            }

            mContext.Schedules.AddRange(data.Schedule);
            mContext.SaveChanges();

            return new ApiResponse<CreateSubjectResultApiModel>
            {
                Response = new CreateSubjectResultApiModel
                {
                    SuccessfulMessage = "Tạo môn học thành công"
                }
            };
        }

        /// <summary>
        /// Gửi yêu cầu tạo dữ liệu đăng ký mới
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        [Route("api/registered")]
        [HttpPost]
        public IActionResult PostRegistered([FromBody] List<RegisteredDataModel> data)
        {
            // Lấy số tín của môn học mà sinh viên đang muốn đăng ký
            int currCredit = mContext.Subject.Where(x => x.Id == data[0].Id).FirstOrDefault().Credit;
            if (CountSumCredits(data[0].Mssv) + currCredit > 22)
            {
                return BadRequest("Không thể đăng ký hơn 22 tín chỉ");
            }

            // Kiểm tra lịch học có bị trùng
            for (int i = 0; i < data.Count; i++)
            {
                string timeStart = mContext.Schedules.Where(x => x.Id == data[i].Id
                                                        && x.DayInTheWeek == data[i].DayInTheWeek)
                                                        .FirstOrDefault().TimeStart;
                string timeFinish = mContext.Schedules.Where(x => x.Id == data[i].Id
                                                            && x.DayInTheWeek == data[i].DayInTheWeek)
                                                            .FirstOrDefault().TimeFinish;

                // Đối chiếu với từng môn học mà sinh viên đã đăng ký
                var db = mContext.Registered.Where(x => x.Mssv == data[i].Mssv).ToList();
                for (int j = 0; j < db.Count; j++)
                {
                    // Nếu khác thứ thì thôi
                    if (!db[j].DayInTheWeek.Equals(data[i].DayInTheWeek)) continue;

                    // Nếu trùng thứ thì kiểm tra giờ
                    string curTimeStart = mContext.Schedules.Where(x => x.Id == db[j].Id
                                                            && x.DayInTheWeek == db[j].DayInTheWeek)
                                                            .FirstOrDefault().TimeStart;
                    string curTimeFinish = mContext.Schedules.Where(x => x.Id == db[j].Id
                                                            && x.DayInTheWeek == db[j].DayInTheWeek)
                                                            .FirstOrDefault().TimeFinish;
                    if ((CompareTime(curTimeStart, timeStart) > 0
                        && CompareTime(timeStart, curTimeFinish) > 0) ||
                        (CompareTime(curTimeStart, timeFinish) > 0
                        && CompareTime(timeFinish, curTimeFinish) > 0))
                    {
                        string error = "Lịch học bị trùng với môn " + db[j].Subject;
                        return BadRequest(error);
                    }
                }
            }
            try
            {
                for (int i = 0; i <data.Count; i++)
                {
                    mContext.Registered.Add(data[i]);
                }
                mContext.SaveChanges();
            }
            catch (Exception ex)
            {
                return BadRequest("Đăng ký môn học thất bại");
            }
            List<string> listResponse = new List<string>() { "Đăng ký môn học thành công" };
            return Ok(listResponse);
        }

        /// <summary>
        /// Hàm so sánh hai chuỗi giờ "hh:mm"
        /// </summary>
        /// <param name="t1"></param>
        /// <param name="t2"></param>
        /// <returns></returns>
        public int CompareTime(string t1, string t2)
        {

            int i = -1;

            int hr1 = Convert.ToInt32(t1.Split(':')[0]);
            int hr2 = Convert.ToInt32(t2.Split(':')[0]);

            int min1 = Convert.ToInt32(t1.Split(':')[1]);
            int min2 = Convert.ToInt32(t2.Split(':')[1]);

            if (hr2 == hr1)
            {
                if (min2 >= min1)
                {
                    i = 1;
                }
            }

            if (hr2 > hr1)
            {
                i = 1;
            }

            return i;
        }

        /// <summary>
        /// Gửi yêu cầu lấy tổng số tín chỉ đã đăng ký
        /// </summary>
        /// <returns></returns>
        [Route("api/subject/credit/{mssv}")]
        [HttpGet]
        public IActionResult GetSumCredits(string mssv)
        {
            return Ok(new {credit = CountSumCredits(mssv) });
        }

        /// <summary>
        /// Hàm tính tổng số tín chỉ hiện tại
        /// </summary>
        /// <param name="mssv"></param>
        /// <returns></returns>
        int CountSumCredits(string mssv)
        {
            // Lấy tất cả môn học mà sinh viên này đã đăng ký
            var db = mContext.Registered.Where(x => x.Mssv == mssv).ToList();
            List<SubjectDataModel> listSubject = new List<SubjectDataModel>();
            for (int i = 0; i < db.Count; i++)
            {
                SubjectDataModel registered = mContext.Subject.Where(x => x.Id == db[i].Id).FirstOrDefault();
                if (!listSubject.Contains(registered)) listSubject.Add(registered);
            }
            return listSubject.Sum(x => x.Credit);
        }

        /// <summary>
        /// Gửi yêu cầu xóa dữ liệu đăng ký
        /// </summary>
        /// <param name="mssv"></param>
        /// <param name="id"></param>
        /// <param name="dayInTheWeek"></param>
        /// <returns></returns>
        [Route("api/registered/{mssv}/{id}")]
        [HttpDelete]
        public IActionResult DeleteRegistered(string mssv, string id)
        {
            try
            {
                // Kiểm tra môn học đã bắt đầu chưa
                string timeStart = mContext.Subject.Where(x => x.Id == id).FirstOrDefault().TimeStart;
                DateTime dateStart = DateTime.ParseExact(timeStart, "MM/dd/yyyy", CultureInfo.CurrentCulture);
                if (dateStart < DateTime.Now)
                {
                    return BadRequest("Môn học đã được bắt đầu");
                }


                var db = mContext.Registered.Where(x => x.Mssv == mssv
                                                        && x.Id == id).ToList();
                if (db != null)
                {
                    mContext.Registered.RemoveRange(db);
                    mContext.SaveChanges();
                }
            }
            catch (Exception ex)
            {
                return BadRequest("Hủy đăng ký môn học thất bại");
            }
            return Ok(new { response = "Hủy đăng ký môn học thành công" });
        }


        ///----------------------------------------------------------------------------------------///

        /// <summary>
        /// Gửi yêu cầu lấy thông tin tất cả khoa
        /// </summary>
        /// <returns></returns>
        [Route("api/subject")]
        [HttpGet]
        public List<String> GetSubjects()
        {
            var listMajor = new List<String>();
            // Xét từng môn học trong csdl
            for (int i = 0; i < mContext.Subject.Count(); i++)
            {
                // Nếu khoa của môn học chưa có trong list thì thêm vào
                String curMajor = mContext.Subject.ToList().ElementAtOrDefault(i).Major;
                if (listMajor.Contains(curMajor) == false)
                {
                    listMajor.Add(curMajor);
                }
            }
            return listMajor;
        }

        /// <summary>
        /// Gửi yêu cầu lấy thông tin một môn học (theo tên hoặc theo id)
        /// </summary>
        /// <param name="string">Tên hoặc id môn học</param>
        /// <returns></returns>
        [HttpGet("api/subject/{subject}")]
        public CreateSubjectCredentialsDataModel GetSubjectById(string subject)
        {
            var model = new CreateSubjectCredentialsDataModel();

            // FirstOrDefault() có nghĩa: Nếu có thì lấy phần tử đầu, không thì null
            model.Subject = mContext.Subject.Where(x => x.Subject == subject).FirstOrDefault();

            // Nếu không có môn nào phù hợp, có thể tham số ở đây là id
            if (model.Subject == null)
            {
                model.Subject = mContext.Subject.Where(x => x.Id == subject).FirstOrDefault();
            }

            // Lấy danh sách lịch học tương ứng
            model.Schedule = mContext.Schedules.Where(x => x.Id == subject).ToList();
            return model;
        }

        /// <summary>
        /// Lấy danh sách tên môn học theo các lựa chọn
        /// </summary>
        /// <param name="major"></param>
        /// <param name="course"></param>
        /// <param name="term"></param>
        /// <param name="mssv"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        [HttpGet("api/subject/{major}/{course}/{term}/{mssv}/{status}")]
        public List<SubjectDTO> GetSubjectDTO(string major
                                               , string course
                                                , string term
                                                 , string mssv
                                                  , int status)
        {
            // Tạo danh sách các data transfer object môn học
            var listSubjectDTO = new List<SubjectDTO>();

            // Lấy danh sách các môn học tương ứng
            List<SubjectDataModel> subjects = mContext.Subject.Where(x => EF.Functions.Like(x.Major, major)
                                                                     && EF.Functions.Like(x.Course, course)
                                                                     && EF.Functions.Like(x.Term, term)
                                                                     && x.Status == status).ToList()
;            // Đưa những dữ liệu thực sự cần thiết vào DTO
            for (int i = 0; i < subjects.Count(); i++)
            {
                var model = new SubjectDTO();

                // Lấy tên môn học
                model.Id = subjects.ElementAtOrDefault(i).Id;
                model.Subject = subjects.ElementAtOrDefault(i).Subject;

                // Kiểm tra id môn học có trong bảng Registered hay không
                if (mContext.Registered.Where(x => x.Mssv == mssv
                                                && x.Id == subjects.ElementAtOrDefault(i).Id).Count() > 0)
                {
                    model.isRegistered = true;
                }
                else
                {
                    model.isRegistered = false;
                }
                listSubjectDTO.Add(model);

            }
            return listSubjectDTO;
        }

        /// <summary>
        /// Get major, name, term, course of subject, Id, teacher
        /// </summary>
        /// <returns></returns>
        [Route("api/subject/listsubject")]
        [HttpGet]
        public ApiResponse<ListSubjectResultApiModel> GetListSubject()
        {
            List<ListSubjectItemDTO> list;

            try
            {
                list = mContext.Subject.Select(item => new ListSubjectItemDTO
                {
                    Major = item.Major,
                    Id = item.Id,
                    Subject = item.Subject,
                    Course = item.Course,
                    Term = item.Term,
                    Teacher = item.Teacher,
                }).ToList();
            }
            catch (Exception ex)
            {
                // if have error, return a error message
                return new ApiResponse<ListSubjectResultApiModel>
                {
                    ErrorMessage = ex.Message,
                };
            }

            // Otherwise, return successful result
            return new ApiResponse<ListSubjectResultApiModel>
            {
                Response = new ListSubjectResultApiModel
                {
                    ListSubject = list,
                }
            };
        }

        [Route("api/subject/updatesubject")]
        [HttpPost]
        public async Task<ApiResponse<int>> UpdateSubjectById([FromBody] CreateSubjectCredentialsDataModel data)
        {
            try
            {
                SubjectDataModel entity = mContext.Subject.Where(item => item.Id.Equals(data.Subject.Id)).FirstOrDefault();
                mContext.Entry(entity).CurrentValues.SetValues(data.Subject);
            }
            catch(Exception ex)
            {
                return new ApiResponse<int>
                {
                    ErrorMessage = ex.Message,
                };
            }

            try
            {
                await mContext.Database.ExecuteSqlCommandAsync($"delete from Schedules where ID = {data.Subject.Id}");
                await mContext.Schedules.AddRangeAsync(data.Schedule);
            }
            catch(Exception ex)
            {
                return new ApiResponse<int>
                {
                    ErrorMessage = ex.Message,
                };
            }

            mContext.SaveChanges();

            return new ApiResponse<int>
            {
                Response = 200,
            };

        }

        [Route("api/subject/statistic")]
        [HttpGet]
        public async Task<ApiResponse<StatisticResultApiModel>> StatisticSubject()
        {
            string query = "select Major, Id, Subject, (select count(*) from (select MSSV, Id from Registered group by MSSV, Id) as [R] where S.Id = R.Id) as total from [Subject] S";
            List<StatisticSubjectItemDTO> data = new List<StatisticSubjectItemDTO>();

            try
            {
                using (var command = mContext.Database.GetDbConnection().CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = System.Data.CommandType.Text;
                    await mContext.Database.OpenConnectionAsync();

                    using (var result = await command.ExecuteReaderAsync())
                    {
                        while (result.Read())
                        {
                            data.Add(new StatisticSubjectItemDTO
                            {
                                Major = result.GetString(0),
                                Id = result.GetString(1),
                                Subject = result.GetString(2),
                                TotalStudent = result.GetInt32(3),
                            });
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                return new ApiResponse<StatisticResultApiModel>
                {
                    ErrorMessage = ex.Message,
                };
            }

            return new ApiResponse<StatisticResultApiModel>
            {
                Response = new StatisticResultApiModel
                {
                    ListSubject = data,
                }
            };
        }

        [Route("api/subject/delete")]
        [HttpPost]
        public async Task<ApiResponse<string>> DeleteSubject([FromBody] string id)
        {
            try
            {
                await mContext.Database.ExecuteSqlCommandAsync($"delete from Schedules where Id = {id}");
                await mContext.Database.ExecuteSqlCommandAsync($"delete from Subject where Id = {id}");
            }
            catch(Exception ex)
            {
                return new ApiResponse<string>
                {
                    ErrorMessage = ex.Message,
                };
            }

            return new ApiResponse<string>
            {
                Response = "Delete success",
            };
        }
    }
}
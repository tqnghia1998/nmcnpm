using Microsoft.EntityFrameworkCore;
using System.ComponentModel.DataAnnotations;

namespace DbModel
{
    public class ExerciseDataModel
    {
        /// <summary>
        /// Mã số sinh viên
        /// </summary>
        [Required]
        public string Mssv { get; set; }

        /// <summary>
        /// Id bài tập
        /// </summary>
        [Required]
        public string Id { get; set; }

        /// <summary>
        /// Tên bài tập
        /// </summary>
        [Required]
        public string Name { get; set; }

        /// <summary>
        /// Tên môn học
        /// </summary>
        [MaxLength(256)]
        public string Subject { get; set; }

        /// <summary>
        /// Deadline
        /// </summary>
        public string Deadline { get; set; }

        /// <summary>
        /// Progress
        /// </summary>
        public string Progress { get; set; }

        /// <summary>
        /// Progress
        /// </summary>
        public string Content { get; set; }

        public ExerciseDataModel() { }

        public ExerciseDataModel(string mssv, string id, string name, string subject, string deadline, string progress, string content)
        {
            Mssv = mssv;
            Id = id;
            Name = name;
            Subject = subject;
            Deadline = deadline;
            Progress = progress;
            Content = content;
        }
    }
}

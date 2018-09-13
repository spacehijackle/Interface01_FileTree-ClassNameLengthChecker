package check;

import java.io.File;

import com.tree.FileTree;
import com.tree.FileTreeCallback;
import com.tree.util.FileTreeUtils;

/**
 * Java クラス名の文字列長チェッカー
 *
 * @author t.yoshida
 */
public class ClassNameLengthChecker
{
	public ClassNameLengthChecker()
	{

	}

	/**
	 * 文字列長のチェックを行う。
	 *
	 * @param rootDir チェック開始フォルダ
	 * @param maxLength 最大文字列長
	 */
	public void check(File rootDir, int maxLength)
	{
		FileTree tree = new FileTree
		(
			rootDir, new CheckerCallback(maxLength)
		);
		tree.scan();
	}

	/**
	 * {@link FileTreeCallback} の実装<br>
	 * クラスファイル文字列長が制限を超えるか否かチェックを行う。
	 */
	class CheckerCallback implements FileTreeCallback
	{
		// 最大文字列長
		int maxLength;

		CheckerCallback(int maxLength)
		{
			this.maxLength = maxLength;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * 拡張子 .java のファイルのみ通知を受ける。
		 * ※フォルダは常に通知を受け取る。
		 * </p>
		 */
		@Override
		public boolean isNotifiable(File target)
		{
			if(target.isFile())
			{
				return (target.getName().endsWith(".java"));
			}

			return true;
		}

		@Override
		public void onScanStarted(File root) { }

		@Override
		public void onDirDetected(File dir, int depth)
		{
			// フォルダ表示
			System.out.println(FileTreeUtils.indent(depth) + "📂 " + dir.getName() + "/");
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * クラス名文字列長が制限を超えている場合、そのクラス名を出力する。
		 * </p>
		 */
		@Override
		public void onFileDetected(File file, int depth)
		{
			String fileName = file.getName();
			String className = fileName.substring(0, fileName.lastIndexOf("."));

			int classNameLength = className.length();
			if(classNameLength > maxLength)
			{
				String indent = FileTreeUtils.indent(depth);
				System.out.println(indent + classNameLength + " | " + className);
			}
		}

		@Override
		public void onScanFinished(File root) { }
	}

	public static void main(String[] args)
	{
		File rootDir = new File(args[0]);
		new ClassNameLengthChecker().check(rootDir, 13);
	}
}

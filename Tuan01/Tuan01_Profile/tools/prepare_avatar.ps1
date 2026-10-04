param(
    [string]$Source = "$PSScriptRoot/../app/src/main/res/drawable/avatar_tran_duc_trung.jpg",
    [string]$Destination = "$PSScriptRoot/../app/src/main/res/drawable/avatar_tran_duc_trung_cutout.png"
)

# Remove only connected white background. Keep the original portrait and RGB pixels.
Add-Type -AssemblyName System.Drawing
Add-Type -ReferencedAssemblies System.Drawing -TypeDefinition @'
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;

public static class PortraitBackground
{
    // Upper outline of the white shirt, measured on the 858 x 1278 source photo.
    // Protect the shirt so its white fabric is not mistaken for the background.
    private static readonly float[] ShirtX = { 0, 130, 205, 225, 260, 290, 570, 625, 656, 750, 858 };
    private static readonly float[] ShirtY = { 875, 825, 787, 760, 735, 712, 712, 750, 785, 810, 855 };

    private static bool IsShirt(int x, int y, int width, int height)
    {
        float sx = x * 858f / width;
        float sy = y * 1278f / height;
        for (int j = 1; j < ShirtX.Length; j++)
        {
            if (sx <= ShirtX[j])
            {
                float t = (sx - ShirtX[j - 1]) / (ShirtX[j] - ShirtX[j - 1]);
                return sy >= ShirtY[j - 1] + t * (ShirtY[j] - ShirtY[j - 1]);
            }
        }
        return false;
    }

    public static string Remove(string sourcePath, string destinationPath)
    {
        using (Bitmap source = new Bitmap(sourcePath))
        using (Bitmap result = new Bitmap(source.Width, source.Height, PixelFormat.Format32bppArgb))
        {
            int width = source.Width, height = source.Height;
            using (Graphics graphics = Graphics.FromImage(result))
                graphics.DrawImageUnscaled(source, 0, 0);

            BitmapData bits = result.LockBits(new Rectangle(0, 0, width, height),
                ImageLockMode.ReadWrite, PixelFormat.Format32bppArgb);
            byte[] pixels = new byte[bits.Stride * height];
            Marshal.Copy(bits.Scan0, pixels, 0, pixels.Length);
            byte[] original = (byte[])pixels.Clone();
            bool[] visited = new bool[width * height];
            Queue<int> queue = new Queue<int>();

            Action<int, int> visit = (x, y) =>
            {
                if (x < 0 || x >= width || y < 0 || y >= height) return;
                int index = y * width + x;
                if (visited[index] || IsShirt(x, y, width, height)) return;
                int offset = y * bits.Stride + x * 4;
                int low = Math.Min(pixels[offset], Math.Min(pixels[offset + 1], pixels[offset + 2]));
                int high = Math.Max(pixels[offset], Math.Max(pixels[offset + 1], pixels[offset + 2]));
                if (low < 242 || high - low > 18) return;
                visited[index] = true;
                queue.Enqueue(index);
            };

            for (int x = 0; x < width; x++) { visit(x, 0); visit(x, height - 1); }
            for (int y = 0; y < height; y++) { visit(0, y); visit(width - 1, y); }
            int transparent = 0;
            while (queue.Count > 0)
            {
                int index = queue.Dequeue();
                int x = index % width, y = index / width;
                pixels[y * bits.Stride + x * 4 + 3] = 0;
                transparent++;
                visit(x - 1, y); visit(x + 1, y);
                visit(x, y - 1); visit(x, y + 1);
            }

            // Verify that every RGB channel, including the face, remains unchanged.
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++)
                    for (int channel = 0; channel < 3; channel++)
                    {
                        int offset = y * bits.Stride + x * 4 + channel;
                        if (pixels[offset] != original[offset])
                            throw new InvalidOperationException("An original RGB pixel changed.");
                    }

            Marshal.Copy(pixels, 0, bits.Scan0, pixels.Length);
            result.UnlockBits(bits);
            result.Save(destinationPath, ImageFormat.Png);
            return width + "x" + height + "; transparent background pixels: " +
                transparent + "; all original RGB pixels preserved.";
        }
    }
}
'@

[PortraitBackground]::Remove(
    [System.IO.Path]::GetFullPath($Source),
    [System.IO.Path]::GetFullPath($Destination)
)

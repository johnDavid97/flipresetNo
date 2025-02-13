import { Inter } from "next/font/google";
import { Providers } from "./providers/providers"; // Update this path
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "FlipReset",
  description: "Live RLCS matches and scores",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <head>
        {/* Favicon */}
        <link rel="icon" href="/logoFane.ico" sizes="any" />
        <link rel="icon" href="/LogoFane.png" type="image/png" />
        <link rel="apple-touch-icon" href="/favicon.png" />
      </head>
      <body className={inter.className}>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
